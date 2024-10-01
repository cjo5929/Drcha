package com.ssafy.drcha.virtualaccount.service;

import com.ssafy.drcha.global.api.RestClientUtil;
import com.ssafy.drcha.global.api.dto.CreateDemandDepositAccountResponse;
import com.ssafy.drcha.global.error.ErrorCode;
import com.ssafy.drcha.global.error.type.IouNotFoundException;
import com.ssafy.drcha.global.error.type.VirtualAccountException;
import com.ssafy.drcha.iou.entity.Iou;
import com.ssafy.drcha.iou.repository.IouRepository;
import com.ssafy.drcha.virtualaccount.entity.VirtualAccount;
import com.ssafy.drcha.virtualaccount.entity.VirtualAccountStatus;
import com.ssafy.drcha.virtualaccount.repository.VirtualAccountRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VirtualAccountService {

    private final RestClientUtil restClientUtil;
    private final VirtualAccountRepository virtualAccountRepository;
    private final IouRepository iouRepository;

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

        CreateDemandDepositAccountResponse response = restClientUtil.createDemandDepositAccount(iou.getCreditor().getUserKey());

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

        virtualAccountRepository.save(virtualAccount);
        iou.linkVirtualAccount(virtualAccount);

        return virtualAccount;
    }


}