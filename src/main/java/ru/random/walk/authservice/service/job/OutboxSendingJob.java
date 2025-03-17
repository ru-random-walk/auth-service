package ru.random.walk.authservice.service.job;

import io.micrometer.tracing.annotation.NewSpan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.random.walk.authservice.model.entity.OutboxMessage;
import ru.random.walk.authservice.repository.OutboxRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@DisallowConcurrentExecution
public class OutboxSendingJob implements Job {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    @NewSpan
    @Transactional
    public void execute(JobExecutionContext context) {
        List<OutboxMessage> messages = outboxRepository.findAllBySentFalse();
        messages.forEach(this::tryToSendMessage);
        outboxRepository.saveAll(messages);
    }

    private void tryToSendMessage(OutboxMessage message) {
        try {
            var topic = message.getTopic();
            var payload = message.getPayload();
            log.info("Sending message to {}, payload: {}", topic, payload);
            kafkaTemplate.send(topic, payload);
            message.setSent(true);
        } catch (Exception e) {
            log.error("Error sending outbox message with id {}", message.getId());
        }
    }
}
