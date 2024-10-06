package com.ssafy.drcha.transaction.repository;

import com.ssafy.drcha.transaction.entity.TransactionHistory;
import com.ssafy.drcha.transaction.entity.TransactionType;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
    boolean existsByTransactionUniqueNo(Long transactionUniqueNo);

    @Query("SELECT th FROM TransactionHistory th " +
            "WHERE th.iou.iouId = :iouId AND th.transactionType = :transactionType " +
            "ORDER BY th.transactionDate DESC")
    List<TransactionHistory> findDepositTransactionsByIouId(@Param("iouId") Long iouId,
                                                            @Param("transactionType") TransactionType transactionType);
}
