package com.ssafy.drcha.transaction.repository;

import com.ssafy.drcha.transaction.entity.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {

//    boolean existsById(Long transactionUniqueNo);

    boolean existsByTransactionUniqueNo(Long transactionUniqueNo);
}
