package ru.random.walk.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.random.walk.authservice.model.entity.OneTimePassword;
import ru.random.walk.authservice.repository.OneTimePasswordRepository;
import ru.random.walk.authservice.service.OneTimePasswordService;
import ru.random.walk.authservice.service.OutboxSenderService;
import ru.random.walk.authservice.service.TemplateService;
import ru.random.walk.dto.SendEmailEvent;
import ru.random.walk.topic.EventTopic;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class OneTimePasswordServiceImpl implements OneTimePasswordService {

    private final OutboxSenderService outboxSenderService;
    private final TemplateService templateService;
    private final OneTimePasswordRepository repository;
    private final PasswordEncoder passwordEncoder;
    private static final String SUBJECT = "Random Walk Authorization";
    private static final int PASSWORD_LENGTH = 6;
    private static final int PASSWORD_TTL_IN_MINUTES = 10;

    @Transactional
    @Override
    public void issueByEmail(String email) {
        log.info("Issue one time password by email");
        String password = generatePassword();
        var oneTimePassword = createOrRefreshPasswordForEmail(email, password);
        sendPasswordByEmail(oneTimePassword, password);
        log.info("One time password is sent");
    }

    @Override
    public boolean isValidPassword(String email, String password) {
        return repository.findById(email)
                .filter(otp -> otp.getExpiresAt().isAfter(LocalDateTime.now()))
                .filter(otp -> passwordEncoder.matches(password, otp.getPassword()))
                .isPresent();
    }

    @Override
    public void deletePasswordByEmail(String email) {
        repository.deleteById(email);
    }

    private OneTimePassword createOrRefreshPasswordForEmail(String email, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        var oneTimePassword = repository.findById(email)
                .orElseGet(() -> createNewPasswordEntity(email));
        oneTimePassword.setPassword(encodedPassword);
        oneTimePassword.setExpiresAt(LocalDateTime.now().plusMinutes(PASSWORD_TTL_IN_MINUTES));
        return repository.save(oneTimePassword);
    }

    private OneTimePassword createNewPasswordEntity(String email) {
        var oneTimePassword = new OneTimePassword();
        oneTimePassword.setEmail(email);
        return oneTimePassword;
    }

    private void sendPasswordByEmail(OneTimePassword oneTimePassword, String notEncodedPassword) {
        SendEmailEvent event = new SendEmailEvent(
                oneTimePassword.getEmail(),
                SUBJECT,
                getBody(notEncodedPassword),
                true
        );
        outboxSenderService.sendMessageByOutbox(EventTopic.SEND_EMAIL, event);
    }

    private String getBody(String password) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("otpCode", password);
        variables.put("codeTtl", PASSWORD_TTL_IN_MINUTES);
        return templateService.processHtmlTemplate("otp_letter_template", variables);
    }

    private String generatePassword() {
        Random random = new SecureRandom();
        StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; ++i) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

}
