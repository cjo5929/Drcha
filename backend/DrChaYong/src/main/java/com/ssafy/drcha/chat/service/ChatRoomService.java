package com.ssafy.drcha.chat.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.drcha.chat.dto.ChatEnterResponseDTO;
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
	public List<ChatEnterResponseDTO> enterChatRoom(Long chatRoomId, String email) {
		ChatRoom chatRoom = findChatRoomById(chatRoomId);
		Member member = findMemberByEmail(email);
		ChatRoomMember chatRoomMember = findChatRoomMember(chatRoom, member);
		List<ChatMessage> messages = chatMongoService.getChatMessages(chatRoomId.toString());
		updateLastReadMessage(chatRoomMember, messages);
		return messages.stream()
			.map(chatMessage -> ChatEnterResponseDTO.from(chatMessage, member.getAvatarUrl()))
			.collect(Collectors.toList());
	}

	@Transactional
	public List<ChatEnterResponseDTO> enterChatRoomViaLink(String invitationLink, UserDetails userDetails) {


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

		List<ChatMessage> messages = chatMongoService.getChatMessages(chatRoom.getChatRoomId().toString());
		ChatMessage enterMessage = createAndSaveEnterMessage(chatRoom, debtor);
		messages.add(enterMessage);

		updateLastReadMessage(chatRoomMember, messages);

		return messages.stream()
			.map(chatMessage -> ChatEnterResponseDTO.from(chatMessage, debtor.getAvatarUrl()))
			.collect(Collectors.toList());
	}


	public ChatRoomEntryStatus processEntryRequest(String invitationLink, String email) {
		Member member = findMemberByEmail(email);

		if (member == null) {
			// 회원이 존재하지 않으면 회원가입 필요
			return ChatRoomEntryStatus.builder()
				.invitationLink(invitationLink)
				.needsRegistration(true)
				.build();
		}

		// 회원이 존재하면 인증 상태 확인
		boolean isVerified = memberService.getVerificationStatusByEmail(email);

		return ChatRoomEntryStatus.builder()
			.invitationLink(invitationLink)
			.needsVerification(!isVerified)
			.build();
	}

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
		Iou iou = iouRepository.findLatestByChatRoomId(chatRoomMember.getChatRoom().getChatRoomId()).orElse(null);
		return ChatRoomListResponseDTO.from(chatRoomMember, iou);
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