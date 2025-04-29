package ru.random.walk.authservice.service;

public interface OneTimePasswordService {
    void issueByEmail(String email);

    boolean isValidPassword(String email, String password);

    void deletePasswordByEmail(String email);
}
