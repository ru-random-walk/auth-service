package ru.random.walk.authservice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.random.walk.authservice.service.KafkaSenderService;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaSenderServiceImpl implements KafkaSenderService {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendMessage(String topic, Object messageObject) {
        try {
            log.info("Sending message to topic: {}. Content: {}", topic, messageObject);
            kafkaTemplate.send(topic, messageObject);
        } catch (Exception e) {
            log.error("Error while sending message to topic {}", topic, e);
        }
    }
}
