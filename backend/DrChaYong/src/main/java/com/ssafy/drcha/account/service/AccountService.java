package com.ssafy.drcha.account.service;

import com.ssafy.drcha.account.dto.AccountResponse;
import com.ssafy.drcha.account.entity.Account;
import com.ssafy.drcha.account.repository.AccountRepository;
import com.ssafy.drcha.global.error.ErrorCode;
import com.ssafy.drcha.global.error.type.UserNotFoundException;
import com.ssafy.drcha.global.util.api.RestClientUtil;
import com.ssafy.drcha.member.entity.Member;
import com.ssafy.drcha.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;
    private final RestClientUtil restClientUtil;

    @Transactional(readOnly = true)
    public List<AccountResponse> getAccountList(String email) {
        Member member = getMemberByEmail(email);
        List<Account> accounts = accountRepository.findAllByMember(member);

        return accounts.stream().map(account -> AccountResponse.builder()
                        .bankName(account.getBankName())
                        .accountNumber(account.getAccountNumber())
                        .accountHolderName(account.getAccountHolderName())
                        .balance(account.getBalance())
                        .isPrimary(account.isPrimary())
                        .build())
                .collect(Collectors.toList());
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
