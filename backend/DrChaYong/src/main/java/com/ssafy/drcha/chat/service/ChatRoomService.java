package com.ssafy.drcha.chat.service;

import com.ssafy.drcha.trust.dto.MemberTrustInfoResponse;
import com.ssafy.drcha.trust.service.MemberTrustService;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.drcha.chat.dto.ChatMessageParam;
import com.ssafy.drcha.chat.dto.ChatMessageResponseDTO;
import com.ssafy.drcha.chat.dto.ChatRoomEntryStatus;
import com.ssafy.drcha.chat.dto.ChatRoomLinkResponseDTO;
import com.ssafy.drcha.chat.dto.ChatRoomListResponseDTO;
import com.ssafy.drcha.chat.entity.ChatMessage;
import com.ssafy.drcha.chat.entity.ChatRoom;
import com.ssafy.drcha.chat.entity.ChatRoomMember;
import com.ssafy.drcha.chat.enums.MemberRole;
import com.ssafy.drcha.chat.factory.ChatMessageFactory;
import com.ssafy.drcha.chat.repository.ChatRoomMemberRepository;
import com.ssafy.drcha.chat.repository.ChatRoomRepository;
import com.ssafy.drcha.global.error.ErrorCode;
import com.ssafy.drcha.global.error.type.BusinessException;
import com.ssafy.drcha.global.error.type.DataNotFoundException;
import com.ssafy.drcha.global.error.type.NeedsRegistrationException;
import com.ssafy.drcha.global.error.type.NeedsVerificationException;
import com.ssafy.drcha.global.error.type.UserNotFoundException;
import com.ssafy.drcha.iou.entity.Iou;
import com.ssafy.drcha.iou.enums.ContractStatus;
import com.ssafy.drcha.iou.repository.IouRepository;
import com.ssafy.drcha.member.entity.Member;
import com.ssafy.drcha.member.repository.MemberRepository;
import com.ssafy.drcha.member.service.MemberService;

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
	public ChatRoomLinkResponseDTO createChatRoom(String email) {
		Member creditor = findMemberByEmail(email);
		ChatRoom chatRoom = ChatRoom.builder()
			.lastMessageId(null)
			.lastMessage(null)
			.lastMessageTime(null)
			.build();
		chatRoom.addMember(creditor, MemberRole.CREDITOR);
		return ChatRoomLinkResponseDTO.from(chatRoomRepository.save(chatRoom));
	}

	@Transactional(readOnly = true)
	public List<ChatRoomListResponseDTO> getChatRoomListByRole(String email, MemberRole role) {
		Member member = findMemberByEmail(email);
		List<ChatRoomMember> chatRoomMembers = chatRoomMemberRepository.findByMemberAndMemberRole(member, role);

		return chatRoomMembers.stream()
			.map(this::createChatRoomListResponseDTO)
			.collect(Collectors.toList());
	}

	@Transactional
	public List<ChatMessageResponseDTO> enterChatRoom(Long chatRoomId, String email) {
		ChatRoom chatRoom = findChatRoomById(chatRoomId);
		Member member = findMemberByEmail(email);
		ChatRoomMember chatRoomMember = findChatRoomMember(chatRoom, member);
		ChatMessageParam param = new ChatMessageParam(0, 20);
		Page<ChatMessage> messages = chatMongoService.getChatScrollMessages(chatRoomId.toString(), param);
		updateLastReadMessage(chatRoomMember, messages.getContent());
		return messages.stream()
			.map(ChatMessageResponseDTO::from)
			.collect(Collectors.toList());
	}

	@Transactional
	public List<ChatMessageResponseDTO> enterChatRoomViaLink(String invitationLink, UserDetails userDetails) {
		ChatRoomEntryStatus entryStatus = processEntryRequest(invitationLink, userDetails.getUsername());

		if (entryStatus.isNeedsRegistration()) {
			throw new NeedsRegistrationException(ErrorCode.MEMBER_NOT_FOUND, invitationLink);
		}

		if (entryStatus.isNeedsVerification()) {
			throw new NeedsVerificationException(ErrorCode.MEMBER_FORBIDDEN_ERROR, invitationLink);
		}

		ChatRoom chatRoom = findChatRoomByInvitationLink(invitationLink);
		Member debtor = findMemberByEmail(userDetails.getUsername());

		validateChatRoomForDebtorEntry(chatRoom);

		ChatRoomMember chatRoomMember = addDebtorToChatRoom(chatRoom, debtor);

		ChatMessageParam param = new ChatMessageParam(0, 20);
		Page<ChatMessage> messages = chatMongoService.getChatScrollMessages(chatRoom.getChatRoomId().toString(), param);
		// ChatMessage enterMessage = createAndSaveEnterMessage(chatRoom, debtor);
		// messages.getContent().add(enterMessage);

		updateLastReadMessage(chatRoomMember, messages.getContent());

		return messages.stream()
			.map(ChatMessageResponseDTO::from)
			.collect(Collectors.toList());
	}

	public ChatRoomEntryStatus processEntryRequest(String invitationLink, String email) {
		Member member = findMemberByEmail(email);

		if (member == null) {
			return ChatRoomEntryStatus.builder()
				.invitationLink(invitationLink)
				.needsRegistration(true)
				.build();
		}

		boolean isVerified = memberService.getVerificationStatusByEmail(email);

		return ChatRoomEntryStatus.builder()
			.invitationLink(invitationLink)
			.needsVerification(!isVerified)
			.build();
	}

	@Transactional(readOnly = true)
	public List<ChatMessageResponseDTO> loadMoreMessages(Long chatRoomId, String email, ChatMessageParam param) {
		Member member = findMemberByEmail(email);

		Page<ChatMessage> messages = chatMongoService.getChatScrollMessages(chatRoomId.toString(), param);

		return messages.stream()
			.map(ChatMessageResponseDTO::from)
			.collect(Collectors.toList());
	}

	/*
	  호출용 메서드
	 */

	private Member findMemberByEmail(String email) {
		return memberRepository.findByEmail(email)
			.orElseThrow(() -> new UserNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
	}

	private ChatRoom findChatRoomById(Long chatRoomId) {
		return chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.CHAT_ROOM_NOT_FOUND));
	}

	private ChatRoom findChatRoomByInvitationLink(String invitationLink) {
		return chatRoomRepository.findByInvitationLink(invitationLink)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.CHAT_ROOM_NOT_FOUND));
	}

	private void validateChatRoomForDebtorEntry(ChatRoom chatRoom) {
		if (chatRoom.getChatRoomMembers().size() >= 2) {
			throw new BusinessException(ErrorCode.CHAT_ROOM_FULL);
		}
	}

	private ChatRoomMember addDebtorToChatRoom(ChatRoom chatRoom, Member debtor) {
		return ChatRoomMember.createMember(debtor, chatRoom, MemberRole.DEBTOR);
	}

	private ChatMessage createAndSaveEnterMessage(ChatRoom chatRoom, Member debtor) {
		ChatMessage enterMessage = ChatMessageFactory.createEnterMessage(chatRoom.getChatRoomId().toString(), debtor);
		chatMongoService.saveChatMessage(enterMessage);
		chatRoom.updateLastMessage(enterMessage.getId(), enterMessage.getContent(), enterMessage.getCreatedAt());
		return enterMessage;
	}

	private ChatRoomListResponseDTO createChatRoomListResponseDTO(ChatRoomMember chatRoomMember) {
		ChatRoom chatRoom = chatRoomMember.getChatRoom();
		Member currentMember = chatRoomMember.getMember();
		Member opponent = findOpponentMember(chatRoom, currentMember);
		Iou iou = iouRepository.findLatestByChatRoomId(chatRoom).orElse(null);

		String contractStatus = getContractStatus(iou);
		Double iouAmount = getIouAmount(iou);
		Long daysUntilDue = getDaysUntilDue(iou);

		MemberTrustInfoResponse memberTrustInfoResponse = memberTrustService.getMemberTrustInfo(chatRoomMember.getMember().getEmail());

		return ChatRoomListResponseDTO.from(
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

	private ChatRoomMember findChatRoomMember(ChatRoom chatRoom, Member member) {
		return chatRoomMemberRepository.findByChatRoomAndMember(chatRoom, member)
			.orElseThrow(() -> new BusinessException(ErrorCode.CHAT_USER_NOT_IN_ROOM));
	}

	private void updateLastReadMessage(ChatRoomMember chatRoomMember, List<ChatMessage> messages) {
		if (!messages.isEmpty()) {
			ChatMessage lastMessage = messages.get(messages.size() - 1);
			chatRoomMember.updateLastRead(lastMessage.getId(), LocalDateTime.now());
		}
	}
}