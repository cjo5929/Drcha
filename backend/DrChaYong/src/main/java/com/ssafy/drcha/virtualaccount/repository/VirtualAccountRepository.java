package com.ssafy.drcha.virtualaccount.repository;

import com.ssafy.drcha.virtualaccount.entity.VirtualAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VirtualAccountRepository extends JpaRepository<VirtualAccount, Long> {

}
