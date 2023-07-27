package com.project.easysign.controller;

import com.project.easysign.domain.Document;
import com.project.easysign.service.DocumentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/document")
public class DocumentController {
    private DocumentService documentService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody Document.Request request){
        String status = documentService.register(request);
        return ResponseEntity.status(HttpStatus.OK).body(status);
    }

    @GetMapping("/view/{id}")
    public void view(@PathVariable("id") Long id){
        log.info("id = {}", id);
        documentService.view(id);
    }



}
