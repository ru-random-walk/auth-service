package ru.random.walk.authservice.config.quartz;

import lombok.RequiredArgsConstructor;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.random.walk.authservice.service.job.OutboxExpireJob;

import java.util.TimeZone;

@Configuration
@RequiredArgsConstructor
public class OutboxExpireJobConfig {

    private static final String JOB_TRIGGER_CRON = "0 0 3 * * ?";

    @Bean
    public JobDetail outboxExpireJobDetail() {
        return JobBuilder.newJob()
                .storeDurably()
                .withIdentity("OutboxExpireJob")
                .ofType(OutboxExpireJob.class)
                .build();
    }

    @Bean
    public Trigger outboxExpireJobTrigger(@Qualifier("outboxExpireJobDetail") JobDetail outboxExpireJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(outboxExpireJobDetail)
                .withIdentity(outboxExpireJobDetail.getKey().getName())
                .withSchedule(
                        CronScheduleBuilder
                                .cronSchedule(JOB_TRIGGER_CRON)
                                .inTimeZone(TimeZone.getTimeZone("UTC"))
                )
                .build();
    }

}
