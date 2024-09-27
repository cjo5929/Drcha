package com.ssafy.drcha.account.service;

import com.ssafy.drcha.account.dto.AccountResponse;
import com.ssafy.drcha.account.dto.SendVerificationCodeResponse;
import com.ssafy.drcha.account.dto.VerifyCodeResponse;
import com.ssafy.drcha.account.entity.Account;
import com.ssafy.drcha.account.repository.AccountRepository;
import com.ssafy.drcha.global.error.ErrorCode;
import com.ssafy.drcha.global.error.type.AccountNotFoundException;
import com.ssafy.drcha.global.error.type.UserNotFoundException;
import com.ssafy.drcha.global.api.RestClientUtil;
import com.ssafy.drcha.global.api.dto.CreateDemandDepositAccountResponse;
import com.ssafy.drcha.global.api.dto.InquireTransactionHistoryResponse;
import com.ssafy.drcha.global.api.dto.OpenAccountAuthResponse;
import com.ssafy.drcha.member.entity.Member;
import com.ssafy.drcha.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;
    private final RestClientUtil restClientUtil;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional(readOnly = true)
    public List<AccountResponse> getAccountList(String email) {
        Member member = getMemberByEmail(email);
        List<Account> accounts = accountRepository.findAllByMember(member);

        return accounts.stream().map(account -> AccountResponse.builder()
                        .bankName(account.getBankName())
                        .accountNumber(account.getAccountNumber())
                        .accountHolderName(member.getUsername())
                        .balance(account.getBalance())
                        .isPrimary(account.isPrimary())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveNewBankAccount(String email) {
        Member member = getMemberByEmail(email);
        CreateDemandDepositAccountResponse response = restClientUtil.createDemandDepositAccount(member.getUserKey());
        accountRepository.save(Account.createAccount(member, response.getRec().getBankCode(), response.getRec().getAccountNo()));
    }

    @Transactional(readOnly = true)
    public SendVerificationCodeResponse sendVerificationCode(String email) {
        String userKey = getMemberByEmail(email).getUserKey();
        List<AccountResponse> accountList = getAccountList(email);
        if (accountList == null || accountList.isEmpty()) {
            throw new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND);
        }
        String accountNo = accountList.get(0).getAccountNumber();

        // 1원 송금 요청
        OpenAccountAuthResponse openAccountAuthResponse = restClientUtil.openAccountAuth(userKey, accountNo);
        restClientUtil.validateApiResponse(openAccountAuthResponse.getHeader());
        log.info("검증 성공");

        // 송금 메시지 조회
        String verificationCode = getVerificationCode(userKey, accountNo, openAccountAuthResponse.getRec().getTransactionUniqueNo());
        verificationCode = verificationCode.length() > 4 ? verificationCode.substring(verificationCode.length() - 4) : verificationCode;
        // redis에 저장
        redisTemplate.opsForValue().set(
                "code:" + email,
                verificationCode,
                3600,
                TimeUnit.SECONDS
        );
        return new SendVerificationCodeResponse(email, verificationCode);
    }

    private String getVerificationCode(String userKey, String accountNo, Long transactionUniqueNo) {
        InquireTransactionHistoryResponse response = restClientUtil.inquireTransactionHistory(userKey, accountNo, transactionUniqueNo);
        return response.getRec().getTransactionSummary();
    }

    @Transactional(readOnly = true)
    public VerifyCodeResponse verifyCode(String email, String code) {
        String verificationCode = redisTemplate.opsForValue().get("code:" + email);
        return new VerifyCodeResponse(email, verificationCode != null && verificationCode.equals(code));
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
