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

    public PageResponse getList(Pageable pageable, Long templateId) {
        Page<Form> formPage = formRepository.findAllByTemplateId(pageable, templateId);
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
    public PageResponse getList(Pageable pageable,Long templateId, String search) {
        Page<Form> formPage = formRepository.findAllByTemplateIdAndVpId(pageable,templateId, search);
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
            decrypted = vc.toString(); // json string 으로 변경
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

//        String publicKey = "" +
//                "MIIBCgKCAQEAuA0INQ7kBzG7ohqe1t2cfnmDx427tFbQHmKTWqzvC2xhxp4HtjLZ" +
//                "XUhFjVO8B1prAqTYM0yfZbntCZmyndT+68YJFViWPfbLQtso/7hF37arnRl7ASZ0" +
//                "PtR5fHtPEFCV8hYbi4hMOivbmxdADU2GNl8Zibayx0sAip4cJnAEWvRpk13BjI7k" +
//                "wSiVyO5DcSfHaN7Nk1rrlmoV0NsjxgumjWdHHrM4IDdop/mv2IJQaGDHLYsfjfpE" +
//                "C5hfLZUp6E3xJxdu4Pp+CKwzix5995YLuHJj6e/oh5+0NsRRSck3bPOYnA2V6kCf" +
//                "3B4PXre+6AwPpv6CIH6JfSH75qNXdI44AQIDAQAB" +
//                "";

        // 1-2. publicKey로 vp 복호화
        JSONObject proof = (JSONObject) vp.get("proof");
        String jws = proof.get("jws").toString();
        log.info("jws => {}", jws);

        decrypted = decrypted.replace("\n", "");
        decrypted = decrypted.replace(" ","");
//        decrypted = decrypted.replace("\"", "");
        decrypted = decrypted.replace("\\", "");
        log.info("vc => {}", decrypted);

//        String jws = "g/RGyO3nzb9t7ElfbEm6Y34y/ozKDrnI4AiymzznR0cadOdJ+aE67LYPOfDXa+z7xQGLjDyv2FaPTHgWLdH3YRtAR9Gx0JSKsodZb7D5fg/90QlCHnqr8yrIy6A+1bnKgf4sJKsFGfItbLc0Ek8iLI05PrCl4+jncJ/o8hinrZDoUF11S/9aK9qG08y8rz1AI+YgKJHliY5bsWNSacIrj1De1Ozoc2eIrZt5Tplmys9+q6I8BQIbzGmtyqJe1LOdMOYWFF1NPGzOZf0LMozZJDYeZ+flrOOI25JVzh7WjwiBK8KYWc0CbUzOpv9QDPTWX43PqxttV8eFob/NJZ3yrg==";
//        String dec = "a";
//        String dec = "{credentialSubject: {id: did:example:74762851-63db-426c-a295-df95353b693e, alumniOf: {phone: 01011111111, name: 박박박, id: did:example:74762851-63db-426c-a295-df95353b693e}}}";
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
