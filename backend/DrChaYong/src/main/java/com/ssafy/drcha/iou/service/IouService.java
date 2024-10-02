package com.ssafy.drcha.iou.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

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
import com.ssafy.drcha.global.error.type.UserNotFoundException;
import com.ssafy.drcha.iou.dto.IouCreateRequestDto;
import com.ssafy.drcha.iou.dto.IouDetailResponseDto;
import com.ssafy.drcha.iou.dto.IouMessageRequestDto;
import com.ssafy.drcha.iou.dto.IouPdfResponseDto;
import com.ssafy.drcha.iou.dto.IouResponseDto;
import com.ssafy.drcha.iou.dto.IouTransactionResponseDto;
import com.ssafy.drcha.iou.entity.Iou;
import com.ssafy.drcha.iou.repository.IouRepository;
import com.ssafy.drcha.member.entity.Member;
import com.ssafy.drcha.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IouService {

	private final IouRepository iouRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final WebClient webClient;
	private final ChatMongoService chatMongoService;
	private final MemberRepository memberRepository;

	@Transactional
	public void createAiIou(Long chatRoomId, String email) {
		ChatRoom chatRoom = getChatRoomById(chatRoomId);
		String messages = chatMongoService.getConversationByChatRoomId(chatRoomId);
		IouCreateRequestDto requestDTO = getIouDetailsFromAI(chatRoomId, messages);
		createAndSaveIou(chatRoom, requestDTO);
	}

	@Transactional
	public void createManualIou(Long chatRoomId, IouCreateRequestDto requestDTO, String email) {
		ChatRoom chatRoom = getChatRoomById(chatRoomId);
		createAndSaveIou(chatRoom, requestDTO);
	}

	@Transactional(readOnly = true)
	public List<IouResponseDto> getIousByChatRoomId(Long chatRoomId, String email) {

		return iouRepository.findByChatRoom_ChatRoomIdOrderByContractStartDateDesc(chatRoomId).stream()
			.map(IouResponseDto::from)
			.collect(Collectors.toList());
	}

	public List<IouTransactionResponseDto> getIousByRole(String email, MemberRole role) {

		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new UserNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

		if (role == MemberRole.CREDITOR) {
			return iouRepository.findByCreditor(member).stream()
				.map(IouTransactionResponseDto::from)
				.collect(Collectors.toList());
		}

		if (role == MemberRole.DEBTOR) {
			return iouRepository.findByDebtor(member).stream()
				.map(IouTransactionResponseDto::from)
				.collect(Collectors.toList());
		}

		throw new DataNotFoundException(ErrorCode.IOU_NOT_FOUND);
	}

	public IouDetailResponseDto getIouDetail(Long iouId, MemberRole role) {
		Iou iou = iouRepository.findById(iouId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.IOU_NOT_FOUND));

		long daysUntilDue = ChronoUnit.DAYS.between(LocalDateTime.now(), iou.getContractEndDate());


		if(role == MemberRole.CREDITOR) {
			return IouDetailResponseDto.from(iou, iou.getDebtor(), daysUntilDue);
		}

		if(role == MemberRole.DEBTOR) {
			return IouDetailResponseDto.from(iou, iou.getCreditor(), daysUntilDue);
		}

		throw  new UserNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
	}

	@Transactional(readOnly = true)
	public IouPdfResponseDto getIouPdfData(Long iouId) {
		return IouPdfResponseDto.from(iouRepository.findById(iouId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.IOU_NOT_FOUND)));
	}

	/*
	  호출용 메서드
	 */
	private ChatRoom getChatRoomById(Long chatRoomId) {
		return chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.CHAT_ROOM_NOT_FOUND));
	}

	private void createAndSaveIou(ChatRoom chatRoom, IouCreateRequestDto requestDTO) {
		Member creditor = findMemberByRole(chatRoom, MemberRole.CREDITOR);
		Member debtor = findMemberByRole(chatRoom, MemberRole.DEBTOR);
		Iou iou = requestDTO.toEntity(creditor, debtor, chatRoom);
		iouRepository.save(iou);
	}

	private Member findMemberByRole(ChatRoom chatRoom, MemberRole role) {
		return chatRoom.getChatRoomMembers().stream()
			.filter(member -> member.getMemberRole() == role)
			.map(ChatRoomMember::getMember)
			.findFirst()
			.orElseThrow(() -> new UserNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
	}

	private IouCreateRequestDto getIouDetailsFromAI(Long chatRoomId, String messages) {
		IouMessageRequestDto requestDto = IouMessageRequestDto.builder()
			.chatRoomId(chatRoomId)
			.messages(messages)
			.build();

		return webClient.post()
			.uri("/extract")
			.bodyValue(requestDto)
			.retrieve()
			.bodyToMono(IouCreateRequestDto.class)
			.block(); // 동기적으로 결과를 받기 위해 block() 호출
	}
}