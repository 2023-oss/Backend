package com.project.easysign.service;

import com.project.easysign.domain.Template;
import com.project.easysign.domain.User;
import com.project.easysign.dto.TemplateDTO;
import com.project.easysign.exception.JsonException;
import com.project.easysign.exception.NonExistentException;
import com.project.easysign.exception.NonExistentUserException;
import com.project.easysign.repository.TemplateRepository;
import com.project.easysign.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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

    public TemplateDTO viewTemplate(String templateId) {
        Template template = templateRepository.findWithUserByTemplateId(templateId)
                .orElseThrow(() -> new NonExistentException("존재하지 않는 동의서 양식 입니다."));
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(template.jsonData);
        } catch (ParseException e) {
            throw new JsonException("json 데이터 파싱에 실패했습니다.");
        }
        String defaultBlock = jsonObject.get("defaultBlock").toString();
        String[] defaults = defaultBlock.split("<br/>");
        JSONArray jsonArray = new JSONArray();
        for(String item : defaults){
            jsonArray.add(item);
        }

        jsonObject.remove("defaultBlock");

        jsonObject.put("defaultBlock", jsonArray);
        log.info("defaults=>{}", jsonObject.get("defaultBlock"));
        String jsonData = jsonObject.toJSONString();
        log.info(jsonData);

        return TemplateDTO.builder()
                .templateId(template.templateId)
                .company(template.user.getCompany())
                .picture(template.user.getPicture())
                .jsonData(jsonData).build();
    }
}
