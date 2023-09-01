package com.project.easysign.service;

import com.project.easysign.domain.Form;
import com.project.easysign.domain.Template;
import com.project.easysign.dto.FormDTO;
import com.project.easysign.dto.PageResponse;
import com.project.easysign.exception.NonExistentTemplateException;
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
    public Object registerForm(Long templateId, String jsonData) throws ParseException, DestroyFailedException {
        // 1. vp 복호화
        // 1-1. block chain에 did 전송 후 privateKey 받기
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(jsonData);
        JSONObject vp = (JSONObject) jsonObject.get("vp");
        JSONArray vcs = (JSONArray) vp.get("verifiableCredential");
        String vp_id = "";
        for(int i=0; i<vcs.size();i++){
            JSONObject vc = (JSONObject) vcs.get(i);
            JSONObject credentialSubject = (JSONObject)vc.get("credentialSubject");
            vp_id = credentialSubject.get("id").toString(); // == did
        }

        Map<String, String> map = restTemplate.getForObject("http://block.platechain.shop/v1/did?did="+vp_id, Map.class);
        log.info("map=>{}",map);
        String publicKey = map.get("publicKey");
        log.info("publicKey=>{}", publicKey);

        // 1-2. publicKey로 vp 복호화
        JSONObject proof = (JSONObject) vp.get("proof");
        String jws = proof.get("jws").toString();
        RsaUtil.decrypt(jws, publicKey);

        // 2. DB에 template_id, vp_id, vp, form, expired_date 저장
        Template template = templateRepository.findById(templateId)
                .orElseThrow(() -> new NonExistentTemplateException());
        String form = jsonObject.get("form").toString();
        formRepository.save(Form.builder()
                .template(template)
                .vpId(vp_id)
                .vp(vp.toString())
                .form(form).build());
        return "SUCCESS";
    }
}
