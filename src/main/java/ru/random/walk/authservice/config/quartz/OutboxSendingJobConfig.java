package ru.random.walk.authservice.config.quartz;

import lombok.RequiredArgsConstructor;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.random.walk.authservice.service.job.OutboxSendingJob;

@Configuration
@RequiredArgsConstructor
public class OutboxSendingJobConfig {

    private static final Integer OUTBOX_JOB_START_INTERVAL_IN_SEC = 5;

    @Bean
    public JobDetail outboxSendingJobDetail() {
        return JobBuilder.newJob()
                .storeDurably()
                .withIdentity("OutboxSendingJob")
                .ofType(OutboxSendingJob.class)
                .build();
    }

    @Bean
    public Trigger outboxSendingJobTrigger(@Qualifier("outboxSendingJobDetail") JobDetail outboxSendingJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(outboxSendingJobDetail)
                .withIdentity(outboxSendingJobDetail.getKey().getName())
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(OUTBOX_JOB_START_INTERVAL_IN_SEC)
                                .repeatForever()
                )
                .build();
    }

}
