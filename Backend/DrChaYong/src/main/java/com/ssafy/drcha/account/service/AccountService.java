package com.ssafy.drcha.account.service;

import com.ssafy.drcha.account.dto.AccountResponse;
import com.ssafy.drcha.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public List<AccountResponse> getAccountList(String email) {
        return null;
    }

}
