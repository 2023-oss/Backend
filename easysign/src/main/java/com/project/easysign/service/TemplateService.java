package com.project.easysign.service;

import com.project.easysign.domain.Template;
import com.project.easysign.domain.User;
import com.project.easysign.exception.NonExistentException;
import com.project.easysign.exception.NonExistentUserException;
import com.project.easysign.repository.TemplateRepository;
import com.project.easysign.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class TemplateService {
    private final TemplateRepository templateRepository;
    private final UserRepository userRepository;
    public String makeTemplate(Long userId, String jsonData) {
        User user = userRepository.findById(userId)
                        .orElseThrow(()-> new NonExistentUserException());
        Template template = templateRepository.findByUser(user)
                .orElseThrow(() -> new NonExistentException("존재하지 않는 템플릿입니다."));
        template.setJsonData(jsonData);
        templateRepository.save(template);
        return "SUCCESS";
    }

    public String updateTemplate(Long templateId, Long userId, String jsonData) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new NonExistentUserException());
        templateRepository.save(Template.builder()
                .id(templateId)
                .user(user)
                .jsonData(jsonData).build());
        return "SUCCESS";
    }

    public String viewTemplate(Long templateId) {
        Template template = templateRepository.findById(templateId)
                .orElseThrow(() -> new NonExistentException("존재하지 않는 동의서 양식 입니다."));
        return template.jsonData;
    }
}
