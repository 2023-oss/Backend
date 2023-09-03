package com.project.easysign.service;

import com.project.easysign.domain.Form;
import com.project.easysign.domain.Template;
import com.project.easysign.dto.FormDTO;
import com.project.easysign.dto.PageResponse;
import com.project.easysign.exception.JsonException;
import com.project.easysign.exception.NonExistentException;
import com.project.easysign.repository.FormRepository;
import com.project.easysign.repository.TemplateRepository;
import com.project.easysign.util.RsaUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import javax.security.auth.DestroyFailedException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class FormService {
    private final FormRepository formRepository;
    private final TemplateRepository templateRepository;
    private final RestTemplate restTemplate;
    // vp 
//    public String makeForm(Long templateId, String imgUrl, FormDTO.Request formDTO, String jsonData) {
//        Template template = templateRepository.findById(templateId)
//                        .orElseThrow(()-> new NonExistentTemplateException());
//        formRepository.save(Form.builder()
//                .template(template)
//                .name(formDTO.getName())
//                .phone(formDTO.getPhone())
//                .sign(imgUrl)
//                .jsonData(jsonData)
//                .build());
//        return "SUCCESS";
//    }

    public PageResponse getList(Pageable pageable) {
        Page<Form> formPage = formRepository.findAll(pageable);
        List<FormDTO.Response> formList = formPage.map(FormDTO.Response::new)
                .stream().collect(Collectors.toList());

        return PageResponse.builder()
                .forms(formList)	// todoDtoPage.getContent()
                .pageNo(pageable.getPageNumber()+1)
                .pageSize(pageable.getPageSize())
                .totalElements(formPage.getTotalElements())
                .totalPages(formPage.getTotalPages())
                .last(formPage.isLast())
                .build();
    }

//    public Object registerForm(Long templateId, String vp_, String form) throws ParseException, DestroyFailedException {
//        // 1. vp 복호화
//        // 1-1. block chain에 did 전송 후 privateKey 받기
//        JSONParser parser = new JSONParser();
//        JSONObject vp = (JSONObject) parser.parse(vp_);
//        JSONObject vc = (JSONObject) vp.get("verifiableCredential");
//        JSONObject credentialSubject = (JSONObject)vc.get("credentialSubject");
//        String vp_id = credentialSubject.get("id").toString(); // == did
//
//        Map<String, String> map = restTemplate.postForObject("http://block.platechain.shop/v1/did?did=did:example:0e97f4ca-1820-4be7-82cb-b4c075b16068", vp_id, Map.class);
//        String publicKey = map.get("publicKey");
//
//        // 1-2. publicKey로 vp 복호화
//        JSONObject proof = (JSONObject) vp.get("proof");
//        String jws = proof.get("jws").toString();
//        RsaUtil.decrypt(jws, publicKey);
//
//        // 2. DB에 template_id, vp_id, vp, form, expired_date 저장
//        Template template = templateRepository.findById(templateId)
//                        .orElseThrow(() -> new NonExistentTemplateException());
//        formRepository.save(Form.builder()
//                .template(template)
//                .vpId(vp_id)
//                .vp(vp_)
//                .form(form).build());
//        return "SUCCESS";
//    }
    public Object registerForm(String templateId, String jsonData){
        // 1. vp 복호화
        // 1-1. block chain에 did 전송 후 privateKey 받기
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(jsonData);
        } catch (ParseException e) {
            throw new JsonException("json 데이터 파싱에 실패했습니다.");
        }
        JSONObject vp = (JSONObject) jsonObject.get("vp");
        JSONArray vcs = (JSONArray) vp.get("verifiableCredential");
        String vp_id = "";
        String decrypted = "";
        for(int i=0; i<vcs.size();i++){
            JSONObject vc = (JSONObject) vcs.get(i);
            decrypted = vc.toString();
            JSONObject credentialSubject = (JSONObject)vc.get("credentialSubject");
            vp_id = credentialSubject.get("id").toString(); // == did
        }

        Map<String, String> map = restTemplate.getForObject("http://block.platechain.shop/v1/did?did="+vp_id, Map.class);
//        Map<String, String> map = restTemplate.getForObject("http://block.platechain.shop/v1/did?did=did:example:4a6593d3-917b-47d7-b0c0-bf39d328a29c", Map.class);
        log.info("map=>{}",map);
        String publicKey = map.get("publicKey");
        log.info("publicKey=>{}", publicKey);
        if(publicKey == null){
            throw new NonExistentException("제출된 vp에 해당하는 publicKey가 존재하지 않습니다.");
        }

        // 1-2. publicKey로 vp 복호화
        JSONObject proof = (JSONObject) vp.get("proof");
        String jws = proof.get("jws").toString();
//        String jws = "dgQKTst4D+e5iwtMT0hoArydZNaSArmNXQy1iaw+uWX52WMUwjiSYU+H3kW1cu3NDYL6PWO8B5AH7aMts/UxqZUSNSr1SDpfTCSFUUmmlqy1c3LkeiNUNbhFFKIRHBROmdL2r3r/GaUOa6A63wvsfFGUKFzj3A7DHVxb/i9LUG7oywSdDPk9UgkvpBLBl0Tm9sJuJDzPr+x7+sW5QMMHZC2Ii6ujeMyFi97EAZccQsiKmob+S6dNJp/KoFiZLGig5QW5V0ow8qCRRUyBHr7ohm+ec1e+J2m3FAPkJLPubbno7To4LgSyZsXZbtUuO9T9JNx8BlyBo1vmE6blve0WhA==";
//        String dec = "{credentialSubject: {id: did:example:4d125fc7-eefc-4d62-ba73-aeb7fa18af19, alumniOf: {phone: 01011111111, name: 박박박, id: did:example:4d125fc7-eefc-4d62-ba73-aeb7fa18af19}}}";
        boolean result = RsaUtil.decrypt(jws, decrypted, publicKey);

        if(!result){
            return "FAIL";
        }
        // 2. DB에 template_id, vp_id, vp, form, expired_date 저장
        Template template = templateRepository.findByTemplateId(templateId)
                .orElseThrow(() -> new NonExistentException("존재하지 않는 동의서 양식 입니다."));
        String form = jsonObject.get("form").toString();
        formRepository.save(Form.builder()
                .template(template)
                .vpId(vp_id)
                .vp(vp.toString())
                .form(form).build());
        return "SUCCESS";
    }

    public Object select(String vpId) {
        Form form = formRepository.findByVpId(vpId)
                .orElseThrow(()-> new NonExistentException("존재하지 않는 동의서입니다."));
        return form.getForm();
    }
}
