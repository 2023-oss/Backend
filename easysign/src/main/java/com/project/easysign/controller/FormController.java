package com.project.easysign.controller;

import com.project.easysign.dto.FormDTO;
import com.project.easysign.dto.PageResponse;
import com.project.easysign.s3.S3Service;
import com.project.easysign.service.FormService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.security.auth.DestroyFailedException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/form")
public class FormController {
    private final FormService formService;
    private final S3Service s3Service;

    @PostMapping("/register/{templateId}")
    public ResponseEntity registerForm(@PathVariable("templateId") String templateId, @RequestBody String jsonData) throws DestroyFailedException, ParseException {
        return ResponseEntity.status(HttpStatus.OK).body(formService.registerForm(templateId, jsonData));
    }

    @GetMapping("/viewAll/{templateId}")
    public ResponseEntity viewAll(@PathVariable("templateId") Long templateId,
                                  @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 5)Pageable pageable){
        PageResponse responses = formService.getList(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/select/{vp_id}")
    public ResponseEntity select(@PathVariable("vp_id") String vp_id){
        return ResponseEntity.status(HttpStatus.OK).body(formService.select(vp_id));
    }

//    @PostMapping("/make/{templateId}")
//    public ResponseEntity makeForm(@PathVariable("templateId")Long templateId,
//                                   @RequestPart("sign") MultipartFile multipartFile,
//                                   @RequestPart("form") FormDTO.Request formDTO,
//                                   @RequestPart("jsonData") String jsonData){
//        String imgUrl = null;
//        if(multipartFile != null){
//            imgUrl = s3Service.uploadImage(multipartFile);
//        }
//        String status = formService.makeForm(templateId, imgUrl, formDTO, jsonData);
//        return ResponseEntity.status(HttpStatus.OK).body(status);
//    }

//    @PostMapping("/register/{templateId}")
//    public ResponseEntity registerForm(@PathVariable("templateId") Long templateId, @RequestBody FormDTO.Request request) throws DestroyFailedException, ParseException {
//        String vp = request.getVp();
//        String form = request.getForm();
//        return ResponseEntity.status(HttpStatus.OK).body(formService.registerForm(templateId, vp, form));
//    }


}
