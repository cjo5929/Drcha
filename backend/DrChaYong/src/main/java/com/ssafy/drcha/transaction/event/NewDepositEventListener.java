package com.ssafy.drcha.transaction.event;

import com.ssafy.drcha.global.api.dto.TransferResponse;
import com.ssafy.drcha.iou.entity.Iou;
import com.ssafy.drcha.transaction.entity.VirtualAccount;
import com.ssafy.drcha.transaction.service.TransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewDepositEventListener {

    private final TransactionService transactionService;

    @Async
    @EventListener
    @Transactional
    public void handleNewDeposit(NewDepositEvent event) {
        log.info("새 입금 이벤트 수신: 차용증 ID {}, 금액 {}", event.getIou().getIouId(), event.getAmount());
        
        Iou iou = event.getIou();
        log.info("새 입금 이벤트로 인한 계좌이체 처리 : 차용증 가상계좌 -> 채권자 계좌로 이체 진행, 금액 : {}", event.getAmount());
        VirtualAccount virtualAccount = iou.getVirtualAccount();
//        log.info("차용증에 대한 가상계좌 : {}", virtualAccount);
        
        try {
            TransferResponse response = transactionService.transferToCreditor(virtualAccount, event.getAmount());
            log.info("채권자 계좌로 이체 성공: {}", response);
        } catch (Exception e) {
            log.error("채권자 계좌로 이체 실패: {}", e.getMessage());
        }
    }
}