package ru.random.walk.authservice.service;

import java.util.Map;

public interface TemplateService {
    String processHtmlTemplate(String templateName, Map<String, Object> variables);
}
