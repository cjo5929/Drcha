package com.ssafy.drcha.global.config;

import com.ssafy.drcha.global.job.VirtualAccountMonitoringJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO : Spring Quartz 스케줄러 설정 클래스
 * * 가상계좌 모니터링 작업을 주기적으로 실행하기 위한 설정 포함
 */
@Configuration
public class QuartzConfig {

    /**
     * ! 가상계좌 모니터링 작업(Job)에 대한 세부 정보 정의
     * @return
     */
    @Bean
    public JobDetail virtualAccountMonitoringJobDetail() {
        return JobBuilder.newJob(VirtualAccountMonitoringJob.class)         // ! 실행할 Job 클래스 지정
                         .withIdentity("virtualAccountMonitoringJob") // ! 이름 설정해야함
                         .storeDurably()                                    // ! Job이 트리거와 연결되지 않아도 저장소에 저장.
                         .build();
    }

    /**
     * TODO : 가상계좌 모니터링 작업의 실행 주기를 정의하는 트리거 생성
     * @return
     */
    @Bean
    public Trigger virtualAccountMonitoringJobTrigger() {
        return TriggerBuilder.newTrigger()
                             .forJob(virtualAccountMonitoringJobDetail())    // ! 위에서 정의한 JobDetail 연결해주기
                             .withIdentity("virtualAccountMonitoringTrigger")
                             .withSchedule(CronScheduleBuilder.cronSchedule("0 */1 * ? * *")) // 1분마다 실행
                             .build();
    }
}
