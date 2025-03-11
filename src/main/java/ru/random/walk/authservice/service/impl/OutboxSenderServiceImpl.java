package ru.random.walk.authservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.random.walk.authservice.model.entity.OutboxMessage;
import ru.random.walk.authservice.repository.OutboxRepository;
import ru.random.walk.authservice.service.OutboxSenderService;

@Service
@Slf4j
@RequiredArgsConstructor
public class OutboxSenderServiceImpl implements OutboxSenderService {

    private final ObjectMapper objectMapper;
    private final OutboxRepository outboxRepository;

    @Override
    @Transactional
    public void sendMessageByOutbox(String topic, Object messageObject) {
        try {
            var outboxMessage = new OutboxMessage();
            outboxMessage.setTopic(topic);
            outboxMessage.setPayload(objectMapper.writeValueAsString(messageObject));
            outboxRepository.save(outboxMessage);
        } catch (JsonProcessingException e) {
            log.error("Error parsing payload for outbox", e);
            throw new RuntimeException(e);
        }
    }
}
