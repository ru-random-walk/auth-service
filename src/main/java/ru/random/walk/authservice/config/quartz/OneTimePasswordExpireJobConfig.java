package ru.random.walk.authservice.config.quartz;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.random.walk.authservice.service.job.OneTimePasswordExpireJob;


@Configuration
public class OneTimePasswordExpireJobConfig {

    private static final int REPEAT_INTERVAL_IN_MINUTES = 15;

    @Bean
    public JobDetail oneTimePasswordExpireJobDetail() {
        return JobBuilder.newJob()
                .storeDurably()
                .withIdentity("OneTimePasswordExpireJob")
                .ofType(OneTimePasswordExpireJob.class)
                .build();
    }

    @Bean
    public Trigger oneTimePasswordExpireJobTrigger(@Qualifier("oneTimePasswordExpireJobDetail") JobDetail oneTimePasswordExpireJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(oneTimePasswordExpireJobDetail)
                .withIdentity(oneTimePasswordExpireJobDetail.getKey().getName())
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .repeatForever()
                                .withIntervalInMinutes(REPEAT_INTERVAL_IN_MINUTES)
                )
                .build();
    }
}
