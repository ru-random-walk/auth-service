package ru.random.walk.authservice.service;

public interface KafkaSenderService {
    void sendMessage(String topic, Object messageObject);
}
