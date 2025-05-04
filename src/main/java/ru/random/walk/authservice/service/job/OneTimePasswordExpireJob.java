package ru.random.walk.authservice.service.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.random.walk.authservice.repository.OneTimePasswordRepository;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class OneTimePasswordExpireJob implements Job {

    private final OneTimePasswordRepository oneTimePasswordRepository;

    @Override
    @Transactional
    public void execute(JobExecutionContext context) {
        log.info("Start executing OneTimePasswordExpireJob");
        int deletedCount = oneTimePasswordRepository.deleteAllByExpiresAtBefore(LocalDateTime.now());
        log.info("Deleted {} one time passwords", deletedCount);
    }
}
