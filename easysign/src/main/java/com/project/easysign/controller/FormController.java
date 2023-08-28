package com.project.easysign.controller;

import com.project.easysign.dto.FormDTO;
import com.project.easysign.dto.PageResponse;
import com.project.easysign.s3.S3Service;
import com.project.easysign.service.FormService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/form")
public class FormController {
    private final FormService formService;
    private final S3Service s3Service;

    @PostMapping("/make/{templateId}")
    public ResponseEntity makeForm(@PathVariable("templateId")Long templateId,
                                   @RequestPart("sign") MultipartFile multipartFile,
                                   @RequestPart("form") FormDTO.Request formDTO,
                                   @RequestPart("jsonData") String jsonData){
        String imgUrl = null;
        if(multipartFile != null){
            imgUrl = s3Service.uploadImage(multipartFile);
        }
        String status = formService.makeForm(templateId, imgUrl, formDTO, jsonData);
        return ResponseEntity.status(HttpStatus.OK).body(status);
    }

    @GetMapping("/viewAll/{templateId}")
    public ResponseEntity viewAll(@PathVariable("templateId") Long templateId,
                                  @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 5)Pageable pageable){
        PageResponse responses = formService.getList(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

}
