package com.project.easysign.controller;

import com.project.easysign.dto.DocumentDto;
import com.project.easysign.dto.TemplateDto;
import com.project.easysign.service.DocumentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/document")
public class DocumentController {
    private DocumentService documentService;

    @GetMapping("/viewAllDocuments/{userId}")
    public ResponseEntity viewAllDocuments(@PathVariable("userId") Long userId){
        List<DocumentDto.Response> documentDtos = documentService.viewAllDocument(userId);
        return ResponseEntity.status(HttpStatus.OK).body(documentDtos);
    }

    @PostMapping("/makeTemplate")
    public ResponseEntity makeTemplate(@RequestBody TemplateDto.Request templateDto){
        String status = documentService.registerTemplate(templateDto);
        return ResponseEntity.status(HttpStatus.OK).body(status);
    }
    @GetMapping("/viewTemplate/{id}")
    public ResponseEntity viewTemplate(@PathVariable("id") Long id){
        TemplateDto.Response templateDto = documentService.viewTemplate(id);
        return ResponseEntity.status(HttpStatus.OK).body(templateDto);
    }
    @DeleteMapping("/deleteTemplate/{id}")
    public ResponseEntity deleteTemplate(@PathVariable("id") Long id){
        String status = documentService.deleteTemplate(id);
        return ResponseEntity.status(HttpStatus.OK).body(status);
    }

    @PostMapping("/makeDocument")
    public ResponseEntity makeDocument(@RequestBody DocumentDto.Request documentDto){
        String status = documentService.registerDocument(documentDto);
        return ResponseEntity.status(HttpStatus.OK).body(status);
    }
    @GetMapping("/viewDocument/{id}")
    public ResponseEntity viewDocument(@PathVariable("id") Long id){
        DocumentDto.Response documentDto = documentService.viewDocument(id);
        return ResponseEntity.status(HttpStatus.OK).body(documentDto);
    }
    @DeleteMapping("/deleteDocument/{id}")
    public ResponseEntity deleteDocument(@PathVariable("id") Long id){
        String status = documentService.deleteTemplate(id);
        return ResponseEntity.status(HttpStatus.OK).body(status);
    }
}
