package ru.random.walk.authservice.service;

public interface OutboxSenderService {

    void sendMessageByOutbox(String topic, Object messageObject);
}
