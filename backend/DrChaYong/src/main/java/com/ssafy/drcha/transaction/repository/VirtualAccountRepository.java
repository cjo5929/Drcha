package com.ssafy.drcha.transaction.repository;

import com.ssafy.drcha.transaction.entity.VirtualAccount;
import com.ssafy.drcha.transaction.entity.VirtualAccountStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VirtualAccountRepository extends JpaRepository<VirtualAccount, Long> {

    List<VirtualAccount> findByStatus(VirtualAccountStatus status);

    Optional<VirtualAccount> findByAccountNumber(String accountNumber);
}
