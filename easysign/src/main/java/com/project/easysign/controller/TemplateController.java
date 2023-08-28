package com.project.easysign.controller;

import com.project.easysign.exception.AuthenticationFailedException;
import com.project.easysign.security.UserPrincipal;
import com.project.easysign.service.TemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/template")
public class TemplateController {
    private final TemplateService templateService;
    @PostMapping("/make")
    public ResponseEntity makeTemplate(@RequestBody String jsonData, @AuthenticationPrincipal UserPrincipal loginUser){
        if(loginUser == null){
            throw new AuthenticationFailedException("로그인 후 사용해주세요.");
        }
        log.info(jsonData);
        String status = templateService.makeTemplate(loginUser.getId(), jsonData);
        return ResponseEntity.status(HttpStatus.OK).body(status);
    }
    @PostMapping("/update/{templateId}")
    public ResponseEntity updateTemplate(@PathVariable("templateId") Long templateId, @RequestBody String jsonData, @AuthenticationPrincipal UserPrincipal loginUser){
        if(loginUser == null){
            throw new AuthenticationFailedException("로그인 후 사용해주세요.");
        }
        String status = templateService.updateTemplate(templateId, loginUser.getId(), jsonData);
        return ResponseEntity.status(HttpStatus.OK).body(status);
    }
    @GetMapping("/view/{templateId}")
    public ResponseEntity viewTemplate(@PathVariable("templateId") Long templateId, @AuthenticationPrincipal UserPrincipal loginUser){
        if(loginUser == null){
            throw new AuthenticationFailedException("로그인 후 사용해주세요.");
        }
        String jsonData = templateService.viewTemplate(templateId);
        return ResponseEntity.status(HttpStatus.OK).body(jsonData);
    }
}
