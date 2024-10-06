package com.ssafy.drcha.transaction.service;

import com.ssafy.drcha.global.api.RestClientUtil;
import com.ssafy.drcha.global.api.dto.TransactionHistoryListResponse;
import com.ssafy.drcha.iou.entity.Iou;
import com.ssafy.drcha.iou.enums.ContractStatus;
import com.ssafy.drcha.iou.service.IouService;
import com.ssafy.drcha.transaction.entity.TransactionHistory;
import com.ssafy.drcha.transaction.entity.VirtualAccount;
import com.ssafy.drcha.transaction.event.NewDepositEvent;
import com.ssafy.drcha.transaction.repository.TransactionHistoryRepository;
import com.ssafy.drcha.transaction.entity.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * * 가상계좌 모니터링 서비스 * 새로운 입금을 확인하고 처리하는 비즈니스 로직 관리
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TransactionMonitoringService {

    private final IouService iouService;
    private final RestClientUtil restClientUtil;
    private final ApplicationEventPublisher eventPublisher;
    private final TransactionHistoryRepository transactionHistoryRepository;

    /**
     * TODO : 모든 활성 가상 계좌의 새로운 입금 확인
     * ! Quartz에 의해 주기적으로 실행됨
     */
    public void checkNewDeposits() {
        log.info("============== 모니터링 시작 =================");
        iouService.findAllActiveIous()
                  .forEach(this::processIouDeposits);
        log.info("============== 모니터링 종료 =================");
    }

    /**
     * TODO : 특정 가상 계좌의 새로운 입금을 처리
     *
     * @param iou 차용증
     */
    private void processIouDeposits(Iou iou) {
        log.info("------ 차용증 ID: {} 처리 시작 ------", iou.getIouId());
        VirtualAccount virtualAccount = iou.getVirtualAccount(); // ! 차용증에 연관된 가상계좌 get
        List<TransactionHistoryListResponse.REC.Transaction> newTransactions = getNewTransactions(virtualAccount);
        log.info("새로운 거래 내역 수: {}", newTransactions.size());

        newTransactions.stream()
                       .filter(this::isNewDeposit)
                       .forEach(transaction -> processNewDeposit(iou, transaction));
        log.info("------ 차용증 ID: {} 처리 완료 ------", iou.getIouId());
    }

    /**
     * TODO : 가상 계좌의 새로운 거래 내역을 조회
     *
     * @param virtualAccount 조회할 가상 계좌
     * @return 새로운 거래 내역 리스트
     */
    private List<TransactionHistoryListResponse.REC.Transaction> getNewTransactions(VirtualAccount virtualAccount) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String today = LocalDate.now().format(formatter);

        TransactionHistoryListResponse response = restClientUtil.inquireTransactionHistoryList(
                virtualAccount.getCreditor().getUserKey(),
                virtualAccount.getAccountNumber(),
                today, // ! 오늘 날짜의 거래만 조회 -> 어차피 주기 줄일 예정
                today,
                "M", // ! 입금만 조회
                "DESC"
        );

        return Optional.ofNullable(response)
                       .map(TransactionHistoryListResponse::getRec)
                       .map(TransactionHistoryListResponse.REC::getList)
                       .orElse(Collections.emptyList());
    }

    /**
     * TODO : 거래가 새로운 입금인지 확인
     *
     * @param transaction 확인할 거래
     * @return 새로운 입금이면 true, 아니면 false
     */
    private boolean isNewDeposit(TransactionHistoryListResponse.REC.Transaction transaction) {
        return "1".equals(transaction.getTransactionType()) && // 입금 타입
                transaction.getTransactionBalance() != null &&
                transaction.getTransactionBalance() > 0 &&
                !transactionHistoryRepository.existsByTransactionUniqueNo(transaction.getTransactionUniqueNo());
    }

    /**
     * TODO : 새로운 입금 처리
     *
     * @param iou 차용증
     * @param transaction 입금 거래
     */
    private void processNewDeposit(Iou iou, TransactionHistoryListResponse.REC.Transaction transaction) {
        BigDecimal depositAmount = BigDecimal.valueOf(transaction.getTransactionBalance());
        log.info("==== 새로운 입금 처리 시작 ====");
        log.info("차용증 ID: {}, 입금액: {}", iou.getIouId(), depositAmount);

        // ! step1. 차용증 잔액 갱신
        log.info("Step 1: 차용증 잔액 갱신");
        iouService.updateIouAfterDeposit(iou, depositAmount);

        // ! step2. 거래 내역 저장
        log.info("Step 2: 거래 내역 저장");
        saveTransactionHistory(iou, transaction);

        // ! step3. 새 입금 이벤트 발행
        log.info("Step 3: 새 입금 이벤트 발행");
        publishNewDepositEvent(iou, depositAmount);

        log.info("==== 새로운 입금 처리 완료 ====");
    }

    /**
     * TODO : 거래내역 저장(추가)
     * @param iou
     * @param transaction
     */
    private void saveTransactionHistory(Iou iou, TransactionHistoryListResponse.REC.Transaction transaction) {
        BigDecimal depositAmount = BigDecimal.valueOf(transaction.getTransactionBalance());
        BigDecimal balanceBeforeTransaction = iou.getBalance().add(depositAmount); // 입금 전 잔액
        BigDecimal balanceAfterTransaction = iou.getBalance(); // 입금 후 잔액 (이미 updateIouAfterDeposit에서 갱신됨)

        LocalDateTime transactionDateTime = parseTransactionDateTime(
                transaction.getTransactionDate(),
                transaction.getTransactionTime()
        );

        TransactionHistory history = TransactionHistory.builder()
                                                       .iou(iou)
                                                       .transactionUniqueNo(transaction.getTransactionUniqueNo())
                                                       .amount(depositAmount)
                                                       .transactionType(TransactionType.DEPOSIT)
                                                       .transactionDate(transactionDateTime)
                                                       .creditorName(iou.getCreditor().getUsername())
                                                       .debtorName(iou.getDebtor().getUsername())
                                                       .description(transaction.getTransactionSummary())
                                                       .balanceBeforeTransaction(balanceBeforeTransaction)
                                                       .balanceAfterTransaction(balanceAfterTransaction)
                                                       .build();

        transactionHistoryRepository.save(history);
        iou.addTransactionHistory(history);
        iouService.save(iou);  // Iou 상태 변경 사항을 저장

        log.info("거래 내역 저장 완료 - 거래 고유번호: {}, 금액: {}, 거래 후 잔액: {}",
                 transaction.getTransactionUniqueNo(), depositAmount, balanceAfterTransaction);

        // Iou가 완료 상태가 되었는지 로그로 기록
        if (iou.getContractStatus() == ContractStatus.COMPLETED) {
            log.info("차용증 ID: {}가 완료 상태로 변경되었습니다.", iou.getIouId());
        }
    }

    private LocalDateTime parseTransactionDateTime(String date, String time) {
        String dateTimeString = date + time;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return LocalDateTime.parse(dateTimeString, formatter);
    }


    /**
     * TODO : 채권자 계좌로 이체 이벤트 발행 -> EventListener 구성
     */
    private void publishNewDepositEvent(Iou iou, BigDecimal amount) {
        NewDepositEvent event = NewDepositEvent.builder()
                                               .iou(iou)
                                               .amount(amount)
                                               .build();
        eventPublisher.publishEvent(event);
        log.info("새 입금 이벤트 발행 - 차용증 ID: {}, 금액: {}", iou.getIouId(), amount);
    }
}