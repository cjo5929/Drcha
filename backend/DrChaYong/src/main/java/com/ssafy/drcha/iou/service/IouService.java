package com.ssafy.drcha.iou.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.ssafy.drcha.chat.entity.ChatRoom;
import com.ssafy.drcha.chat.entity.ChatRoomMember;
import com.ssafy.drcha.chat.enums.MemberRole;
import com.ssafy.drcha.chat.repository.ChatRoomRepository;
import com.ssafy.drcha.chat.service.ChatMongoService;
import com.ssafy.drcha.global.error.ErrorCode;
import com.ssafy.drcha.global.error.type.DataNotFoundException;
import com.ssafy.drcha.iou.dto.IouCreateResponseDTO;
import com.ssafy.drcha.iou.dto.IouMessageRequestDTO;
import com.ssafy.drcha.iou.repository.IouRepository;
import com.ssafy.drcha.member.entity.Member;
import com.ssafy.drcha.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IouService {

	private final IouRepository iouRepository;
	private final MemberRepository memberRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final WebClient webClient;
	private final ChatMongoService chatMongoService;

	@Transactional
	public void createIou(Long chatRoomId) {

		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.CHAT_ROOM_NOT_FOUND));

		String messages = chatMongoService.getConversationByChatRoomId(chatRoomId);

		IouCreateResponseDTO responseDto = getIouDetailsFromAI(chatRoomId, messages);

		Member creditor = null;
		Member debtor = null;

		for (ChatRoomMember member : chatRoom.getChatRoomMembers()) {
			if (member.getMemberRole() == MemberRole.CREDITOR) {
				creditor = member.getMember();
			} else if (member.getMemberRole() == MemberRole.DEBTOR) {
				debtor = member.getMember();
			}
		}

		if (creditor == null || debtor == null) {
			throw new DataNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
		}

		iouRepository.save(responseDto.toEntity(creditor, debtor));
	}

	public IouCreateResponseDTO getIouDetailsFromAI(Long chatRoomId, String messages) {
		IouMessageRequestDTO requestDto = IouMessageRequestDTO.builder()
			.chatRoomId(chatRoomId)
			.messages(messages)
			.build();

		// Flask AI 서버에 POST 요청을 보내 데이터를 가져오는 부분
		return webClient.post()
			.uri("/extract")
			.bodyValue(requestDto)
			.retrieve()
			.bodyToMono(IouCreateResponseDTO.class)
			.block();// 동기적으로 결과를 받기 위해 block() 호출
	}
}