package com.ssafy.drcha.chat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.drcha.chat.dto.ChatRoomLinkResponseDTO;
import com.ssafy.drcha.chat.entity.ChatMessage;
import com.ssafy.drcha.chat.entity.ChatRoom;
import com.ssafy.drcha.chat.enums.MemberRole;
import com.ssafy.drcha.chat.repository.ChatRoomRepository;
import com.ssafy.drcha.global.error.ErrorCode;
import com.ssafy.drcha.global.error.type.BusinessException;
import com.ssafy.drcha.global.error.type.DataNotFoundException;
import com.ssafy.drcha.global.error.type.UserNotFoundException;
import com.ssafy.drcha.member.entity.Member;
import com.ssafy.drcha.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {

	private final ChatRoomRepository chatRoomRepository;
	private final MemberRepository memberRepository;
	private final ChatService chatService;

	/*
	채팅방 초기 생성
	 */
	@Transactional
	public ChatRoomLinkResponseDTO createChatRoom(String email) {
		Member creditor = memberRepository.findByEmail(email)
			.orElseThrow(() -> new UserNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

		ChatRoom chatRoom = ChatRoom.builder()
			.lastMessageId(null)
			.lastMessage(null)
			.lastMessageTime(null)
			.build();

		log.info(creditor.getEmail());
		chatRoom.addMember(creditor, MemberRole.CREDITOR);
		ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

		return ChatRoomLinkResponseDTO.from(savedChatRoom);
	}

	/*
	채무자가 초대 링크로 채팅방에 참여하는 로직
	 */
	@Transactional
	public void addDebtorToChatRoom(Long chatRoomId, String email) {
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.CHAT_ROOM_NOT_FOUND));

		Member debtor = memberRepository.findByEmail(email)
			.orElseThrow(() -> new UserNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

		boolean alreadyInRoom = chatRoom.getChatRoomMembers().stream()
			.anyMatch(member -> member.getMember().getId().equals(debtor.getId()));

		if (alreadyInRoom) {
			throw new BusinessException(ErrorCode.CHAT_USER_ALREADY_IN_ROOM);
		}

		chatRoom.addMember(debtor, MemberRole.DEBTOR);

		ChatMessage enterMessage = chatService.createEnterMessage(chatRoomId.toString(), debtor);
		chatRoom.updateLastMessage(enterMessage.getId(), enterMessage.getContent(), enterMessage.getCreatedAt());

		chatRoomRepository.save(chatRoom);
	}

	/*
	 채팅방 목록 조회
	 */

}