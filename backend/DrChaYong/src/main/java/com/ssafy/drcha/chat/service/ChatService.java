package com.ssafy.drcha.chat.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.drcha.chat.dto.ChatMessageRequestDto;
import com.ssafy.drcha.chat.dto.ChatMessageResponseDto;
import com.ssafy.drcha.chat.entity.ChatMessage;
import com.ssafy.drcha.chat.entity.ChatRoom;
import com.ssafy.drcha.chat.entity.ChatRoomMember;
import com.ssafy.drcha.chat.enums.ChatMessageType;
import com.ssafy.drcha.chat.repository.ChatRoomMemberRepository;
import com.ssafy.drcha.chat.repository.ChatRoomRepository;
import com.ssafy.drcha.global.error.ErrorCode;
import com.ssafy.drcha.global.error.type.DataNotFoundException;
import com.ssafy.drcha.global.error.type.UserNotFoundException;
import com.ssafy.drcha.iou.dto.IouPdfResponseDto;
import com.ssafy.drcha.member.entity.Member;
import com.ssafy.drcha.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

	private final RabbitTemplate rabbitTemplate;
	private final ChatMongoService chatMongoService;
	private final SimpMessageSendingOperations messagingTemplate;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatRoomMemberRepository chatRoomMemberRepository;
	private final MemberRepository memberRepository;

	private static final String CHAT_EXCHANGE_NAME = "chat.exchange";
	private static final String ROUTING_KEY = "chat.message.";

	@Transactional
	public void sendChatMessage(ChatMessageRequestDto message) {
		log.info(message.toString());
		ChatMessage chatMessageEntity = message.toEntity();
		ChatMessageResponseDto responseDTO = ChatMessageResponseDto.from(chatMongoService.saveChatMessage(chatMessageEntity));

		updateChatRoomLastMessage(responseDTO.getChatRoomId(), responseDTO.getId(), responseDTO.getContent(), responseDTO.getCreatedAt());
		incrementUnreadCount(responseDTO.getChatRoomId(), responseDTO.getSenderId());

		publishMessage(responseDTO);
	}


	@Transactional
	public void sendIouDetailsMessage(Long chatRoomId, IouPdfResponseDto iouPdfResponseDto) {
		// 차용증 관련 상세 정보를 포함한 ChatMessage 생성
		ChatMessage chatMessage = ChatMessage.builder()
			.chatRoomId(String.valueOf(chatRoomId))
			.content("차용증이 생성되었습니다. 상세 내용을 확인하세요.")
			.messageType(ChatMessageType.IOU)
			.senderId("0")
			.iouInfo(iouPdfResponseDto)
			.build();

		ChatMessageResponseDto responseDTO = ChatMessageResponseDto.from(chatMongoService.saveChatMessage(chatMessage));

		updateChatRoomLastMessage(responseDTO.getChatRoomId(), responseDTO.getId(), responseDTO.getContent(), responseDTO.getCreatedAt());

		publishMessage(responseDTO);
	}



	@Transactional
	public void updateChatRoomLastMessage(Long chatRoomId, String messageId, String message, LocalDateTime messageTime) {
		ChatRoom chatRoom = getChatRoomById(chatRoomId);
		chatRoom.updateLastMessage(messageId, message, messageTime);
	}

	@Transactional
	public void incrementUnreadCount(Long chatRoomId, Long senderId) {
		ChatRoomMember recipientMember = chatRoomMemberRepository.findByChatRoom_ChatRoomIdAndMember_IdNot(
				chatRoomId, senderId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

		recipientMember.incrementUnreadCount();
	}

	@Transactional(readOnly = true)
	public List<ChatMessageResponseDto> loadAllMessagesAndSend(Long chatRoomId, String email) {

		ChatRoom chatRoom = getChatRoomById(chatRoomId);

		boolean isMember = chatRoom.getChatRoomMembers().stream()
			.anyMatch(chatRoomMember -> chatRoomMember.getMember().getUsername().equals(email));

		if (!isMember) {
			throw new UserNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
		}

		List<ChatMessageResponseDto> messages = chatMongoService.getAllMessages(String.valueOf(chatRoomId)).stream()
			.map(ChatMessageResponseDto::from)
			.collect(Collectors.toList());

		markMessagesAsRead(chatRoomId, email, messages);

		return messages;
	}

	@Transactional
	public void markMessagesAsRead(Long chatRoomId, String email, List<ChatMessageResponseDto> messages) {
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.CHAT_ROOM_NOT_FOUND));

		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

		ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByChatRoomAndMember(chatRoom, member)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

		// 마지막 메시지를 기준으로 읽음 처리
		if (!messages.isEmpty()) {
			ChatMessageResponseDto lastMessage = messages.get(messages.size() - 1);
			chatRoomMember.updateLastRead(lastMessage.getId(), LocalDateTime.now());
		}
	}


	private ChatRoom getChatRoomById(Long chatRoomId) {
		return chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.CHAT_ROOM_NOT_FOUND));
	}


	private void publishMessage(ChatMessageResponseDto message) {
		rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, ROUTING_KEY + message.getChatRoomId(), message);
	}

	@RabbitListener(queues = "chat.queue")
	public void receiveMessage(ChatMessageResponseDto message) {
		messagingTemplate.convertAndSend("/topic/chat." + message.getChatRoomId(), message);
	}




}