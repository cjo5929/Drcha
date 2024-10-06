package com.ssafy.drcha.global.job;

import com.ssafy.drcha.transaction.service.TransactionMonitoringService;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

/**
 * * 가상계좌 모니터링을 위한 Quartz Job 클래스
 * * 스케줄에 따라 자동으로 실행되어 새로운 입금 확인하기
 */
@Component
@RequiredArgsConstructor
public class VirtualAccountMonitoringJob implements Job {

    private final TransactionMonitoringService monitoringService;

    /**
     * TODO : Job 실행 메서드
     * -> 스케줄러에 의해 호출되어 새로운 입금 확인
     * 이미 설정한 Quartz 스케줄러에 의해 주기적으로 실행됨
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        monitoringService.checkNewDeposits();
    }
}