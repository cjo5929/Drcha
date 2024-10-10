package com.ssafy.drcha.chat.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.drcha.chat.dto.ChatRoomEntryResponseDto;
import com.ssafy.drcha.chat.dto.ChatRoomEntryStatus;
import com.ssafy.drcha.chat.dto.ChatRoomLinkResponseDto;
import com.ssafy.drcha.chat.dto.ChatRoomListResponseDto;
import com.ssafy.drcha.chat.entity.ChatMessage;
import com.ssafy.drcha.chat.entity.ChatRoom;
import com.ssafy.drcha.chat.entity.ChatRoomMember;
import com.ssafy.drcha.chat.enums.MemberRole;
import com.ssafy.drcha.chat.repository.ChatRoomMemberRepository;
import com.ssafy.drcha.chat.repository.ChatRoomRepository;
import com.ssafy.drcha.global.error.ErrorCode;
import com.ssafy.drcha.global.error.type.DataNotFoundException;
import com.ssafy.drcha.global.error.type.NeedsRegistrationException;
import com.ssafy.drcha.global.error.type.NeedsVerificationException;
import com.ssafy.drcha.global.error.type.NotFoundDebtorException;
import com.ssafy.drcha.global.error.type.UserNotFoundException;
import com.ssafy.drcha.iou.entity.Iou;
import com.ssafy.drcha.iou.enums.ContractStatus;
import com.ssafy.drcha.iou.repository.IouRepository;
import com.ssafy.drcha.member.entity.Member;
import com.ssafy.drcha.member.repository.MemberRepository;
import com.ssafy.drcha.member.service.MemberService;
import com.ssafy.drcha.trust.dto.MemberTrustInfoResponse;
import com.ssafy.drcha.trust.service.MemberTrustService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

	private final ChatRoomRepository chatRoomRepository;
	private final MemberRepository memberRepository;
	private final ChatRoomMemberRepository chatRoomMemberRepository;
	private final IouRepository iouRepository;
	private final ChatMongoService chatMongoService;
	private final MemberService memberService;
	private final MemberTrustService memberTrustService;

	@Transactional
	public ChatRoomLinkResponseDto createChatRoom(String email) {
		Member creditor = findMemberByEmail(email);
		ChatRoom chatRoom = ChatRoom.builder()
			.lastMessageId(null)
			.lastMessage(null)
			.lastMessageTime(null)
			.build();
		chatRoom.addMember(creditor, MemberRole.CREDITOR);
		return ChatRoomLinkResponseDto.from(chatRoomRepository.save(chatRoom));
	}

	@Transactional(readOnly = true)
	public List<ChatRoomListResponseDto> getChatRoomListByRole(String email, MemberRole role) {
		Member member = findMemberByEmail(email);
		List<ChatRoomMember> chatRoomMembers = chatRoomMemberRepository.findByMemberAndMemberRole(member, role);

		return chatRoomMembers.stream()
			.map(this::createChatRoomListResponseDTO)
			.filter(Objects::nonNull)
			.collect(Collectors.toList());
	}

	@Transactional
	public ChatRoomEntryResponseDto enterChatRoom(Long chatRoomId, String email) {
		ChatRoom chatRoom = findChatRoomById(chatRoomId);
		Member member = findMemberByEmail(email);
		Member creditor = findCreditorMember(chatRoom);
		ChatRoomMember chatRoomMember = findChatRoomMember(chatRoom, creditor, member);
		Member debtor = findDebtorMember(chatRoom);
		Member opponent = findOpponentMember(chatRoom, member);



		updateLastReadMessage(chatRoomMember, chatMongoService.getLastMessages(chatRoomId.toString(), 1), chatRoomId);

		return ChatRoomEntryResponseDto.from(chatRoom, chatRoomMember, opponent, creditor, debtor);
	}


	@Transactional
	public ChatRoomEntryResponseDto enterChatRoomViaLink(Long chatRoomId, UserDetails userDetails) {
		ChatRoomEntryStatus entryStatus = processEntryRequest(chatRoomId, userDetails.getUsername());

		if (entryStatus.isNeedsRegistration()) {
			throw new NeedsRegistrationException(ErrorCode.MEMBER_NOT_FOUND);
		}

		if (entryStatus.isNeedsVerification()) {
			throw new NeedsVerificationException(ErrorCode.MEMBER_FORBIDDEN_ERROR);
		}


		ChatRoom chatRoom = findChatRoomByInvitationLink(chatRoomId);

		Member debtor = findMemberByEmail(userDetails.getUsername());

		ChatRoomMember chatRoomMember = addDebtorToChatRoom(chatRoom, debtor);
		Member creditor = findCreditorMember(chatRoom);

		Member opponent = findOpponentMember(chatRoom, debtor);



		updateLastReadMessage(chatRoomMember, chatMongoService.getLastMessages(chatRoom.getChatRoomId().toString(), 1), chatRoom.getChatRoomId());

		return ChatRoomEntryResponseDto.from(chatRoom, chatRoomMember, opponent, creditor, debtor);
	}

	public ChatRoomEntryStatus processEntryRequest(Long chatRoomId, String email) {
		Member member = findMemberByEmail(email);

		if (member == null) {
			return ChatRoomEntryStatus.builder()
				.chatRoomId(chatRoomId)
				.needsRegistration(true)
				.build();
		}

		boolean isVerified = memberService.getVerificationStatusByEmail(email);

		return ChatRoomEntryStatus.builder()
			.chatRoomId(chatRoomId)
			.needsVerification(!isVerified)
			.build();
	}


	/*
	  호출용 메서드
	 */

	// 채권자 찾는 메소드
	private Member findCreditorMember(ChatRoom chatRoom) {
		return chatRoom.getChatRoomMembers().stream()
			.filter(chatRoomMember -> chatRoomMember.getMemberRole() == MemberRole.CREDITOR)
			.map(ChatRoomMember::getMember)
			.findFirst()
			.orElse(null);
	}

	// 채무자 찾는 메소드
	private Member findDebtorMember(ChatRoom chatRoom) {
		return chatRoom.getChatRoomMembers().stream()
			.filter(chatRoomMember -> chatRoomMember.getMemberRole() == MemberRole.DEBTOR)
			.map(ChatRoomMember::getMember)
			.findFirst()
			.orElse(null);
	}

	private Member findMemberByEmail(String email) {
		return memberRepository.findByEmail(email)
			.orElseThrow(() -> new UserNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
	}

	private ChatRoom findChatRoomById(Long chatRoomId) {
		return chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.CHAT_ROOM_NOT_FOUND));
	}

	private ChatRoom findChatRoomByInvitationLink(Long chatRoomId) {
		return chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.CHAT_ROOM_NOT_FOUND));
	}


	private ChatRoomMember addDebtorToChatRoom(ChatRoom chatRoom, Member debtor) {
		return ChatRoomMember.createMember(debtor, chatRoom, MemberRole.DEBTOR);
	}

	private ChatRoomListResponseDto createChatRoomListResponseDTO(ChatRoomMember chatRoomMember) {
		ChatRoom chatRoom = chatRoomMember.getChatRoom();
		Member currentMember = chatRoomMember.getMember();
		Member opponent = findOpponentMember(chatRoom, currentMember);
		if(opponent == null) {
			return null;
		}
		Optional<Iou> latestIou = iouRepository.findLatestByChatRoomId(chatRoom.getChatRoomId());

		Iou iou = latestIou.orElse(null);

		String contractStatus = getContractStatus(iou);
		Double iouAmount = getIouAmount(iou);
		Long daysUntilDue = getDaysUntilDue(iou);

		MemberTrustInfoResponse memberTrustInfoResponse = memberTrustService.getMemberTrustInfo(chatRoomMember.getMember().getEmail());

		return ChatRoomListResponseDto.from(
			chatRoom,
			opponent,
			contractStatus,
			iouAmount,
			daysUntilDue,
			memberTrustInfoResponse,
			chatRoomMember.getUnreadCount()
		);
	}

	private Member findOpponentMember(ChatRoom chatRoom, Member currentMember) {
		return chatRoom.getChatRoomMembers().stream()
			.map(ChatRoomMember::getMember)
			.filter(member -> !member.equals(currentMember))
			.findFirst()
			.orElse(null);
	}

	private String getContractStatus(Iou iou) {
		return (iou != null) ? iou.getContractStatus().name() : ContractStatus.DRAFTING.name();
	}

	private Double getIouAmount(Iou iou) {
		return (iou != null) ? iou.getIouAmount().doubleValue() : null;
	}

	private Long getDaysUntilDue(Iou iou) {
		if (iou != null && iou.getContractEndDate() != null) {
			return ChronoUnit.DAYS.between(LocalDateTime.now(), iou.getContractEndDate());
		}
		return null;
	}

	private ChatRoomMember findChatRoomMember(ChatRoom chatRoom, Member creditor, Member debtor) {
		log.info(chatRoom.getChatRoomId() + "  " + debtor.getUsername());
		return chatRoomMemberRepository.findByChatRoomAndMember(chatRoom, debtor)
			.orElseThrow(() -> new NotFoundDebtorException(ErrorCode.CHAT_USER_NOT_IN_ROOM, creditor.getId().toString()));
	}

	private void updateLastReadMessage(ChatRoomMember chatRoomMember, List<ChatMessage> messages, Long chatRoomId) {
		if (!messages.isEmpty()) {
			ChatMessage lastMessage = messages.get(messages.size() - 1);
			chatRoomMember.updateLastRead(lastMessage.getId(), LocalDateTime.now());
			ChatRoom chatRoom = getChatRoomById(chatRoomId);
			chatRoom.updateLastMessage(lastMessage.getId(), lastMessage.getContent(), LocalDateTime.now());
		}
	}

	private ChatRoom getChatRoomById(Long chatRoomId) {
		return chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.CHAT_ROOM_NOT_FOUND));
	}
}