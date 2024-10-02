package com.ssafy.drcha.trust.service;

import static com.ssafy.drcha.global.error.ErrorCode.MEMBER_NOT_FOUND;

import com.ssafy.drcha.global.error.type.UserNotFoundException;
import com.ssafy.drcha.trust.enums.TrustInfoMessage;
import com.ssafy.drcha.trust.dto.MemberTrustInfoResponse;
import com.ssafy.drcha.trust.entity.MemberTrust;
import com.ssafy.drcha.trust.repository.MemberTrustRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberTrustService {

    private final MemberTrustRepository memberTrustRepository;

    /**
     * 주어진 회원 email에 해당하는 회원의 신뢰도 정보를 조회합니다.
     *
     * @param email 조회할 회원의 email
     * @return 회원의 신뢰도 정보를 담은 MemberTrustInfoResponse 객체
     * @throws UserNotFoundException 해당 ID의 회원이 존재하지 않을 경우 발생
     */
    public MemberTrustInfoResponse getMemberTrustInfo(String email) {
        MemberTrust memberTrust = memberTrustRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(MEMBER_NOT_FOUND));

        int lateTrades = memberTrust.getCurrentLateTrades();
        int debtTrades = memberTrust.getCurrentDebtTrades();
        int completedTrades = memberTrust.getCompletedTrades();

        String message = determineTraderStatus(lateTrades, debtTrades, completedTrades);

        return MemberTrustInfoResponse.withTradeHistory(lateTrades, debtTrades, message);
    }

    /**
     * 회원의 거래 이력을 바탕으로 거래자 상태 메시지를 결정합니다.
     *
     * @param lateTrades 지연된 거래 횟수
     * @param debtTrades 채무 거래 횟수
     * @param completedTrades 완료된 거래 횟수
     * @return 거래자 상태를 나타내는 메시지
     */
    private String determineTraderStatus(int lateTrades, int debtTrades, int completedTrades) {
        if (lateTrades == 0 && debtTrades == 0 && completedTrades == 0) {
            return TrustInfoMessage.FIRST_TIME_TRADER.getMessage();
        }
        if (lateTrades == 0 && debtTrades == 0) {
            return TrustInfoMessage.SAFE_TRADER.getMessage();
        }
        return TrustInfoMessage.WARNING_TRADER.getMessage();
    }
}
