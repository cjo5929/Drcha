package com.ssafy.drcha.iou.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClient;

import com.ssafy.drcha.chat.entity.ChatRoom;
import com.ssafy.drcha.chat.entity.ChatRoomMember;
import com.ssafy.drcha.chat.enums.MemberRole;
import com.ssafy.drcha.chat.repository.ChatRoomRepository;
import com.ssafy.drcha.chat.service.ChatMongoService;
import com.ssafy.drcha.chat.service.ChatService;
import com.ssafy.drcha.global.error.ErrorCode;
import com.ssafy.drcha.global.error.type.DataNotFoundException;
import com.ssafy.drcha.global.error.type.UserNotFoundException;
import com.ssafy.drcha.iou.dto.FinancialCalculator;
import com.ssafy.drcha.iou.dto.IouCreateAiRequestDto;
import com.ssafy.drcha.iou.dto.IouCreateRequestDto;
import com.ssafy.drcha.iou.dto.IouCreateResponseDto;
import com.ssafy.drcha.iou.dto.IouDetailResponseDto;
import com.ssafy.drcha.iou.dto.IouPdfResponseDto;
import com.ssafy.drcha.iou.dto.IouResponseDto;
import com.ssafy.drcha.iou.dto.IouTransactionResponseDto;
import com.ssafy.drcha.iou.entity.Iou;
import com.ssafy.drcha.iou.enums.ContractStatus;
import com.ssafy.drcha.iou.repository.IouRepository;
import com.ssafy.drcha.member.entity.Member;
import com.ssafy.drcha.member.repository.MemberRepository;
import com.ssafy.drcha.transaction.entity.VirtualAccount;
import com.ssafy.drcha.transaction.service.TransactionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class IouService {

	private final IouRepository iouRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final WebClient webClient;
	private final ChatMongoService chatMongoService;
	private final MemberRepository memberRepository;
	private final TransactionService transactionService;
	private final ChatService chatService;

	@Transactional
	public void createAiIou(Long chatRoomId, String email) {
		ChatRoom chatRoom = getChatRoomById(chatRoomId);

		log.info("@@@@@@@@@@@@ 통과");
		String messages = chatMongoService.getConversationByChatRoomId(chatRoomId);

		log.info("$$$$$$$$$$$$ 통과");
		IouCreateRequestDto requestDTO = getIouDetailsFromAI(chatRoomId, messages);

		log.info("########## 통과");

		String creditorName = null;
		String debtorName = null;

		for (ChatRoomMember member : chatRoom.getChatRoomMembers()) {
			if (member.getMemberRole() == MemberRole.CREDITOR) {
				creditorName = member.getMember().getUsername();
			} else if (member.getMemberRole() == MemberRole.DEBTOR) {
				debtorName = member.getMember().getUsername();
			}
		}

		Iou savedIou = createAndSaveIou(chatRoom, requestDTO);

		IouCreateResponseDto responseDto = new IouCreateResponseDto(
			savedIou.getIouId(),
			creditorName,
			debtorName,
			savedIou.getIouAmount(),
			savedIou.getContractStartDate(),
			savedIou.getContractEndDate(),
			savedIou.getInterestRate(),
			savedIou.getBorrowerAgreement(),
			savedIou.getLenderAgreement(),
			FinancialCalculator.calculateTotalAmount(savedIou.getIouAmount(), savedIou.getInterestRate(), 12)

		);

		chatService.sendIouDetailsMessage(chatRoomId, responseDto);

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


	@Transactional
	public void agreeToIou(Long iouId, String email) {
		Iou iou = iouRepository.findById(iouId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.IOU_NOT_FOUND));

		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new UserNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

		if (iou.getCreditor().equals(member)) {
			iou.lenderAgree();
		} else if (iou.getDebtor().equals(member)) {
			iou.borrowerAgree();
		} else {
			throw new DataNotFoundException(ErrorCode.IOU_NOT_FOUND);
		}

		iouRepository.save(iou);
	}

	/*
	  호출용 메서드
	 */
	private ChatRoom getChatRoomById(Long chatRoomId) {
		return chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.CHAT_ROOM_NOT_FOUND));
	}

	@Transactional
	public Iou createAndSaveIou(ChatRoom chatRoom, IouCreateRequestDto requestDTO) {
		Member creditor = findMemberByRole(chatRoom, MemberRole.CREDITOR);
		Member debtor = findMemberByRole(chatRoom, MemberRole.DEBTOR);
		Iou iou = requestDTO.toEntity(creditor, debtor, chatRoom);
		Iou savedIou = iouRepository.save(iou);

		// ! 가상계좌 생성 및 연결
		if (ObjectUtils.isEmpty(savedIou.getVirtualAccount())) {
			VirtualAccount virtualAccount = transactionService.createVirtualAccount(savedIou.getIouId());
			savedIou.linkVirtualAccount(virtualAccount);
		}

		return savedIou;
	}

	private Member findMemberByRole(ChatRoom chatRoom, MemberRole role) {
		return chatRoom.getChatRoomMembers().stream()
			.filter(member -> member.getMemberRole() == role)
			.map(ChatRoomMember::getMember)
			.findFirst()
			.orElseThrow(() -> new UserNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
	}

	private IouCreateRequestDto getIouDetailsFromAI(Long chatRoomId, String messages) {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("conversation", messages);

		IouCreateAiRequestDto aiResponse = webClient.post()
			.uri("/extract")
			.bodyValue(requestBody)
			.retrieve()
			.bodyToMono(IouCreateAiRequestDto.class)
			.block();

		log.info(aiResponse.toString());

		if (aiResponse == null) {
			throw new IllegalArgumentException("Failed to receive response from AI server.");
		}

		return aiResponse.toIouCreateRequestDto();
	}


	// ========  입금 모니터링 관련 ====== //
	public List<Iou> findAllActiveIous() {
		return iouRepository.findAllByContractStatus(ContractStatus.ACTIVE);
	}

	public void updateIouAfterDeposit(Iou iou, BigDecimal depositAmount) {
		iou.updateBalance(depositAmount);
		save(iou);
	}

	public void save(Iou iou) {
		iouRepository.save(iou);
	}
}