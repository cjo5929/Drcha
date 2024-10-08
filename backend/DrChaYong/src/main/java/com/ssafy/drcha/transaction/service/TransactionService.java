package com.ssafy.drcha.transaction.service;

import com.ssafy.drcha.account.entity.Account;
import com.ssafy.drcha.account.repository.AccountRepository;
import com.ssafy.drcha.global.api.RestClientUtil;
import com.ssafy.drcha.global.api.dto.CreateDemandDepositAccountResponse;
import com.ssafy.drcha.global.api.dto.DepositResponse;
import com.ssafy.drcha.global.api.dto.TransferResponse;
import com.ssafy.drcha.global.api.dto.WithdrawResponse;
import com.ssafy.drcha.global.error.ErrorCode;
import com.ssafy.drcha.global.error.type.AccountNotFoundException;
import com.ssafy.drcha.global.error.type.InsufficientBalanceException;
import com.ssafy.drcha.global.error.type.IouNotFoundException;
import com.ssafy.drcha.global.error.type.UserNotFoundException;
import com.ssafy.drcha.global.error.type.VirtualAccountException;
import com.ssafy.drcha.iou.entity.Iou;
import com.ssafy.drcha.iou.enums.ContractStatus;
import com.ssafy.drcha.iou.repository.IouRepository;
import com.ssafy.drcha.member.entity.Member;
import com.ssafy.drcha.member.repository.MemberRepository;
import com.ssafy.drcha.transaction.dto.DepositRequestDto;
import com.ssafy.drcha.transaction.dto.IouTransactionHistoryResponse;
import com.ssafy.drcha.transaction.dto.TransferRequestDto;
import com.ssafy.drcha.transaction.dto.WithdrawRequestDto;
import com.ssafy.drcha.transaction.entity.TransactionHistory;
import com.ssafy.drcha.transaction.entity.TransactionType;
import com.ssafy.drcha.transaction.entity.VirtualAccount;
import com.ssafy.drcha.transaction.entity.VirtualAccountStatus;
import com.ssafy.drcha.transaction.event.NewDepositEvent;
import com.ssafy.drcha.transaction.repository.TransactionHistoryRepository;
import com.ssafy.drcha.transaction.repository.VirtualAccountRepository;
import com.ssafy.drcha.trust.entity.MemberTrust;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class TransactionService {

    private final RestClientUtil restClientUtil;
    private final VirtualAccountRepository virtualAccountRepository;
    private final IouRepository iouRepository;
    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * TODO : 무통장 계좌(가상계좌) 생성
     * ! RestClientUtil을 사용하여 실제 가상계좌 생성 API 호출 및 생성된 가상계좌 정보 VirtualAccount 엔티티에 저장
     * ! 관련 엔티티와 연관관계 설정
     * @param iouId
     * @return
     */
    public VirtualAccount createVirtualAccount(Long iouId) {
        log.info("===============    가상 계좌 생성    ====================");
        Iou iou = iouRepository.findById(iouId)
                               .orElseThrow(() -> new IouNotFoundException(ErrorCode.IOU_NOT_FOUND));

        if (ObjectUtils.isNotEmpty(iou.getVirtualAccount())) {
            throw new VirtualAccountException(ErrorCode.VIRTUAL_ACCOUNT_ALREADY_EXISTS); // ! 이미 존재하면 안돼
        }
        log.info("------- 이 부분 확인 --------");
        CreateDemandDepositAccountResponse response = restClientUtil.createDemandDepositAccount(iou.getCreditor().getUserKey());  // ! 채권자 userKey로 가상계좌 생성.
        log.info("------- 이 부분 통과 ----------");
        VirtualAccount virtualAccount = VirtualAccount.builder()
                  .iou(iou)
                  .creditor(iou.getCreditor())
                  .debtor(iou.getDebtor())
                  .accountNumber(response.getRec().getAccountNo())
                  .bankCode(response.getRec().getBankCode())
                  .balance(BigDecimal.ZERO)
                  .totalAmount(BigDecimal.valueOf(iou.getIouAmount()))
                  .remainingAmount(BigDecimal.valueOf(iou.getIouAmount()))
                  .status(VirtualAccountStatus.ACTIVE)
                  .build();

        save(virtualAccount);
        iou.linkVirtualAccount(virtualAccount);

        // 채무 거래 업데이트
        updateCurrentDebtTrade(iou.getDebtor().getMemberTrust());

        return virtualAccount;
    }

    private void updateCurrentDebtTrade(MemberTrust memberTrust){
        memberTrust.incrementCurrentDebtTrades();
    }

    public List<VirtualAccount> findAllActiveAccounts() {
        return virtualAccountRepository.findByStatus(VirtualAccountStatus.ACTIVE);
    }

    public void save(VirtualAccount virtualAccount) {
        virtualAccountRepository.save(virtualAccount);
    }

    /**
     * TODO : 회원 계좌 입금 처리
     *
     * @param email 입금 요청한 회원의 email
     * @param depositRequestDto 입금 요청 정보
     * @return DepositResponse
     */
    public DepositResponse depositMemberAccount(String email, DepositRequestDto depositRequestDto) {
        log.info("---------- 사용자 계좌 입금 처리 시작: 사용자 {}, 요청 {} ---------", email, depositRequestDto);

        Member member = getMemberByEmail(email);
        log.info("accountNo : {}", depositRequestDto.getAccountNo());
        Account account = getMemberAccountByAccountNumber(depositRequestDto.getAccountNo());

        DepositResponse response = restClientUtil.deposit(
                member.getUserKey(),
                depositRequestDto.getAccountNo(),
                depositRequestDto.getTransactionBalance(),
                depositRequestDto.getTransactionSummary()
        );

        // ! 계좌 잔액 업데이트
        BigDecimal newBalance = account.getBalance().add(BigDecimal.valueOf(depositRequestDto.getTransactionBalance()));
        account.changeBalance(newBalance);
        accountRepository.save(account);

        log.info("------------ 사용자 계좌 입금 처리 완료: {} ------------", response);
        return response;
    }


    /**
     * TODO : 가상 계좌 입금 처리
     *
     * @param email 입금 요청한 회원의 email
     * @param depositRequestDto 입금 요청 정보
     * @return DepositResponse
     */
    public DepositResponse depositVirtualAccount(String email, DepositRequestDto depositRequestDto) {
        log.info("---------- 계좌 입금 처리 시작: 사용자 {}, 요청 {} ---------", email, depositRequestDto);

        Member member = getMemberByEmail(email);
        log.info("accountNo : {}", depositRequestDto.getAccountNo());
        VirtualAccount account = getVirtualAccountByAccountNumber(depositRequestDto.getAccountNo());

        DepositResponse response = restClientUtil.deposit(
                member.getUserKey(),
                depositRequestDto.getAccountNo(),
                depositRequestDto.getTransactionBalance(),
                depositRequestDto.getTransactionSummary()
        );

        /**
         * ! XXX
         * 굳이 검증 X
         */
//        log.info("-------- 이제 validate 진입 -------");
//        log.info("입금 결과 확인 : {}", response);
//        restClientUtil.validateApiResponse(response.getHeaderResponse());
//        log.info("-------- 여기서 터짐 ---------");
        // ! 계좌 잔액 업데이트
        BigDecimal newBalance = account.getBalance().add(BigDecimal.valueOf(depositRequestDto.getTransactionBalance()));
        account.setBalance(newBalance);
        virtualAccountRepository.save(account);

        log.info("------------ 가상 계좌 입금 처리 완료: {} ------------", response);
        return response;
    }


    /**
     * TODO : 가상 계좌 출금 처리 -> userKey를 채권자의 userKey로 진행해야함.
     *
     * @param email 출금 요청한 회원의 email
     * @param withdrawRequestDto 출금 요청 정보
     * @return WithdrawResponse
     */
    public WithdrawResponse withdraw(String email, WithdrawRequestDto withdrawRequestDto) {
        log.info("---------- 계좌 출금 처리 시작: 사용자 {}, 요청 {} ---------", email, withdrawRequestDto);

        Member member = getMemberByEmail(email);
        VirtualAccount account = getVirtualAccountByAccountNumber(withdrawRequestDto.getAccountNo());

        // 출금 가능 여부 확인
        if (account.getBalance().compareTo(BigDecimal.valueOf(withdrawRequestDto.getTransactionBalance())) < 0) {
            throw new InsufficientBalanceException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        WithdrawResponse response = restClientUtil.withdraw(
                account.getCreditor().getUserKey(),
                withdrawRequestDto.getAccountNo(),
                withdrawRequestDto.getTransactionBalance(),
                withdrawRequestDto.getTransactionSummary()
        );

        // 계좌 잔액 업데이트
        BigDecimal newBalance = account.getBalance().subtract(BigDecimal.valueOf(withdrawRequestDto.getTransactionBalance()));
        account.setBalance(newBalance);
        virtualAccountRepository.save(account);

        log.info("------------ 계좌 출금 처리 완료: {} ------------", response);
        return response;
    }

    /**
     * TODO : 계좌 이체 처리 -> 채권자의 userKey 사용 : test
     *
     * @param email 이체 요청한 회원의 email
     * @param transferRequestDto 이체 요청 정보
     * @return TransferResponse
     */
    public TransferResponse transfer(String email, TransferRequestDto transferRequestDto) {
        log.info("---------- 계좌 이체 처리 시작: 사용자 {}, 요청 {} ---------", email, transferRequestDto);

        Member member = getMemberByEmail(email);
        VirtualAccount fromAccount = getVirtualAccountByAccountNumber(transferRequestDto.getWithdrawalAccountNo());
        VirtualAccount toAccount = getVirtualAccountByAccountNumber(transferRequestDto.getDepositAccountNo());

        // ! 이체 가능 여부 확인
        if (fromAccount.getBalance().compareTo(BigDecimal.valueOf(transferRequestDto.getTransactionBalance())) < 0) {
            throw new InsufficientBalanceException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        // ! 채권자의 userKey 사용
        TransferResponse response = restClientUtil.transfer(
                fromAccount.getCreditor().getUserKey(),
                transferRequestDto.getWithdrawalAccountNo(),
                transferRequestDto.getDepositAccountNo(),
                transferRequestDto.getTransactionBalance(),
                transferRequestDto.getDepositTransactionSummary(),
                transferRequestDto.getWithdrawalTransactionSummary()
        );

        // ! 출금 계좌 잔액 업데이트
        BigDecimal transferAmount = BigDecimal.valueOf(transferRequestDto.getTransactionBalance());
        BigDecimal newFromBalance = fromAccount.getBalance().subtract(transferAmount);
        fromAccount.setBalance(newFromBalance);
        virtualAccountRepository.save(fromAccount);

        // ! 입금 계좌 잔액 업데이트
        BigDecimal newToBalance = toAccount.getBalance().add(transferAmount);
        toAccount.setBalance(newToBalance);
        virtualAccountRepository.save(toAccount);

        log.info("------------ 계좌 이체 처리 완료: {} ------------", response);
        return response;
    }

    /**
     * TODO : 앞단에서 사용할 채무자 -> 가상계좌 계좌이체
     *
     * @param iouId 차용증 ID
     * @param amount 갚을 금액
     * @return TransferResponse
     */
    @Transactional
    public TransferResponse repayDebt(String email, Long iouId, BigDecimal amount) {
        Iou iou = iouRepository.findById(iouId)
                               .orElseThrow(() -> new IouNotFoundException(ErrorCode.IOU_NOT_FOUND));
        VirtualAccount virtualAccount = iou.getVirtualAccount(); // 차용증에 대한 가상계좌
        if (ObjectUtils.isEmpty(virtualAccount)) {
            throw new VirtualAccountException(ErrorCode.VIRTUAL_ACCOUNT_NOT_FOUND);
        }

        Account debtorAccount = iou.getDebtor().getAccount();   // 채무자 계좌
        if (ObjectUtils.isEmpty(debtorAccount)) {
            throw new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
        }

        log.info("============ 차용증 : {}==============", iou.getIouId());
        log.info("============ 차용증에 대한 가상계좌 : {}", virtualAccount.getAccountNumber());
        log.info("============ 채무자 계좌 : {}, 채무자 ID : {}, 채무자 이름 : {}, 채권자 ID : {}, 채권자 이름 : {}, 채무자 이름 : {}", debtorAccount.getAccountNumber(), debtorAccount.getMember().getId(), debtorAccount.getMember().getUsername(), iou.getCreditor().getId(), iou.getCreditor().getUsername(), iou.getDebtor().getUsername());
        // ! 채무자의 계좌에서 이체 가능 여부 확인
        if (debtorAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        // ! 이체 요청 생성
        TransferRequestDto transferRequestDto = TransferRequestDto.builder()
                                                                  .withdrawalAccountNo(debtorAccount.getAccountNumber())
                                                                  .depositAccountNo(virtualAccount.getAccountNumber())
                                                                  .transactionBalance(amount.longValue())
                                                                  .depositTransactionSummary("채무 상환")
                                                                  .withdrawalTransactionSummary("채무 상환")
                                                                  .build();

        // ! 채무자의 userKey를 사용하여 이체 실행 -> 출금 기준 userKey를 사용 ?
        TransferResponse response = restClientUtil.transfer(
                iou.getDebtor().getUserKey(),
                transferRequestDto.getWithdrawalAccountNo(),
                transferRequestDto.getDepositAccountNo(),
                transferRequestDto.getTransactionBalance(),
                transferRequestDto.getDepositTransactionSummary(),
                transferRequestDto.getWithdrawalTransactionSummary()
        );

        // ! 이체 성공 시 채무자 계좌와 가상계좌 잔액 업데이트
        if ("H0000".equals(response.getHeaderResponse().getResponseCode())) {
            // ! 채무자 계좌 잔액 감소
            BigDecimal newDebtorAccountBalance = debtorAccount.getBalance().subtract(amount);
            debtorAccount.changeBalance(newDebtorAccountBalance);
            accountRepository.save(debtorAccount);

            // ! 차용증에 대한 가상계좌 잔액 증가
            BigDecimal newVirtualAccountBalance = virtualAccount.getBalance().add(amount);
            virtualAccount.setBalance(newVirtualAccountBalance);
            virtualAccountRepository.save(virtualAccount);

            // ! 차용증 잔액 갱신
            iou.updateBalance(amount); // ! 만약 잔액 0이하 -> 여기서 COMPLETED로 처리
            iouRepository.save(iou);

            // ! 거래 내역 저장
            saveTransactionHistory(iou, response, amount);

            // ! 이벤트 발행
            publishNewDepositEvent(iou, amount);


        } else {
            log.error("이체 실패: {}", response.getHeaderResponse().getResponseMessage());
            throw new RuntimeException("이체 실패: " + response.getHeaderResponse().getResponseMessage());
        }

        log.info("채무 상환을 위한 이체 완료: 차용증 ID {}", iouId);
        return response;
    }

    private void saveTransactionHistory(Iou iou, TransferResponse response, BigDecimal amount) {
        BigDecimal balanceBeforeTransaction = iou.getBalance().add(amount); // 입금 전 잔액
        BigDecimal balanceAfterTransaction = iou.getBalance(); // 입금 후 잔액

        TransferResponse.TransactionRecord transactionRecord = response.getRec().get(0); // 첫 번째 거래 기록 사용(출금 -> 채무자의 계좌에서 출금이니까)

        TransactionHistory history = TransactionHistory.builder()
                .iou(iou)
                .transactionUniqueNo(transactionRecord.getTransactionUniqueNo())
                .amount(amount)
                .transactionType(TransactionType.DEPOSIT)
                .transactionDate(parseTransactionDate(transactionRecord.getTransactionDate()))
                .creditorName(iou.getCreditor().getUsername())
                .debtorName(iou.getDebtor().getUsername())
                .description("채무 상환")
                .balanceBeforeTransaction(balanceBeforeTransaction)
                .balanceAfterTransaction(balanceAfterTransaction)
                .build();

        transactionHistoryRepository.save(history);
        iou.addTransactionHistory(history);

        log.info("거래 내역 저장 완료 - 거래 고유번호: {}, 금액: {}, 거래 후 잔액: {}",
                transactionRecord.getTransactionUniqueNo(), amount, balanceAfterTransaction);

        if (iou.getContractStatus() == ContractStatus.COMPLETED) {
            log.info("차용증 ID: {}가 완전히 상환되었습니다. 상환 날짜 : {}", iou.getIouId(), iou.getRepaymentDate());
            // ! 채무자, 채권자에게 알림 보낼 것인지 ?
        }
    }

    private LocalDateTime parseTransactionDate(String transactionDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(transactionDate, formatter).atStartOfDay();
    }

    private void publishNewDepositEvent(Iou iou, BigDecimal amount) {
        NewDepositEvent event = NewDepositEvent.builder()
                .iou(iou)
                .amount(amount)
                .build();
        eventPublisher.publishEvent(event);
//        log.info("새 입금 이벤트 발행 - 차용증 ID: {}, 금액: {}", iou.getIouId(), amount);
    }

    /**
     * TODO : 입금모니터링에서 사용할 가상계좌 -> 채권자 계좌이체
     *
     * @param virtualAccount 출금할 가상계좌
     * @param amount 이체 금액
     * @return TransferResponse
     */
    public TransferResponse transferToCreditor(VirtualAccount virtualAccount, BigDecimal amount) {
        log.info("가상계좌에서 채권자 계좌로 이체 시작: 가상계좌 {}, 금액 {}", virtualAccount.getAccountNumber(), amount);

        Member creditor = virtualAccount.getCreditor();
        Account creditorAccount = creditor.getAccount();

        if (creditorAccount == null) {
            throw new RuntimeException("채권자의 계좌 정보가 없습니다.");
        }

        String creditorAccountNumber = creditorAccount.getAccountNumber();

        // ! 이체 가능 여부 확인
        if (virtualAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        // ! 이체 요청 생성
        TransferRequestDto transferRequestDto = TransferRequestDto.builder()
                                                                  .withdrawalAccountNo(virtualAccount.getAccountNumber())
                                                                  .depositAccountNo(creditorAccountNumber)
                                                                  .transactionBalance(amount.longValue())
                                                                  .depositTransactionSummary("채무 상환")
                                                                  .withdrawalTransactionSummary("채무 상환")
                                                                  .build();

        // ! 채권자의 userKey를 사용하여 이체 실행
        TransferResponse response = restClientUtil.transfer(
                creditor.getUserKey(),
                transferRequestDto.getWithdrawalAccountNo(),
                transferRequestDto.getDepositAccountNo(),
                transferRequestDto.getTransactionBalance(),
                transferRequestDto.getDepositTransactionSummary(),
                transferRequestDto.getWithdrawalTransactionSummary()
        );

        // ! 가상계좌 잔액 업데이트
        BigDecimal newVirtualAccountBalance = virtualAccount.getBalance().subtract(amount);
        virtualAccount.setBalance(newVirtualAccountBalance);
        virtualAccount.setRemainingAmount(virtualAccount.getRemainingAmount().subtract(amount));
        virtualAccountRepository.save(virtualAccount);

        // ! 채권자 실제 계좌 잔액 업데이트
        BigDecimal newCreditorAccountBalance = creditorAccount.getBalance().add(amount);
        creditorAccount.changeBalance(newCreditorAccountBalance);
        accountRepository.save(creditorAccount);

        log.info("가상계좌에서 채권자 계좌로 이체 완료: {}", response);
        return response;
    }

    /**
     * TODO : 차용증에 대한 입금 거래내역 리스트 반환
     * @param iouId 차용증 ID
     * @return 입금 거래내역 리스트
     */
    public IouTransactionHistoryResponse getDepositTransactionHistory(Long iouId) {
        log.info("차용증 입금 거래 내역 조회 - 차용증 ID: {}", iouId);

        Iou iou = iouRepository.findById(iouId)
                               .orElseThrow(() -> new IouNotFoundException(ErrorCode.IOU_NOT_FOUND));

        List<TransactionHistory> transactionHistories = transactionHistoryRepository
                .findDepositTransactionsByIouId(iouId, TransactionType.DEPOSIT);

        log.info("조회된 입금 거래 내역 수: {}", transactionHistories.size());
        return IouTransactionHistoryResponse.from(iou, transactionHistories);
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                               .orElseThrow(() -> new UserNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private VirtualAccount getVirtualAccountByAccountNumber(String accountNumber) {
        return virtualAccountRepository.findByAccountNumber(accountNumber)
                                .orElseThrow(() -> new VirtualAccountException(ErrorCode.VIRTUAL_ACCOUNT_NOT_FOUND, "Account number : " + accountNumber + " not found"));
    }

    private Account getMemberAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));
    }
}