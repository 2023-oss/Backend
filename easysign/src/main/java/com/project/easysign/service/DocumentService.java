package com.project.easysign.service;

import com.project.easysign.domain.Answer;
import com.project.easysign.domain.Block;
import com.project.easysign.domain.Document;
import com.project.easysign.domain.User;
import com.project.easysign.dto.*;
import com.project.easysign.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class DocumentService {
    private DocumentRepository documentRepository;
    private BlockRepository blockRepository;
    private AnswerRepository answerRepository;
    private UserRepository userRepository;

    public String registerTemplate(TemplateDto.Request templateDto) {
        User user = userRepository.findById(templateDto.getUserId())
                        .orElseThrow(()-> new RuntimeException("존재하지 않는 사용자입니다."));
        Document document = documentRepository.save(templateDto.toEntity(user));
        List<TemplateCategoryDto> categoryDtos = templateDto.getCategoryDtos();
        categoryDtos.forEach(v->{
            String category = v.getCategoryType();
            List<BlockDto> blockDtos = v.getBlockDtos();
            registerBlocks(category, blockDtos, document);
        });
        return "SUCCESS";
    }
    private void registerBlocks(String category, List<BlockDto> blockDtos, Document document) {
        blockDtos.forEach(v->{
            blockRepository.save(v.toEntity(document, category));
        });
    }

    public String registerDocument(DocumentDto.Request documentDto) {
        User user = userRepository.findById(documentDto.getUserId())
                .orElseThrow(()-> new RuntimeException("존재하지 않는 사용자입니다."));
        Document document = documentRepository.save(documentDto.toEntity(user));
        List<DocumentCategoryDto> categoryDtos = documentDto.getCategoryDtos();
        categoryDtos.forEach(v->{
            String category = v.getCategoryType();
            List<AnswerDto> answerDtos = v.getAnswerDtos();
            registerAnswers(category, answerDtos, document);
        });
        return "SUCCESS";
    }

    private void registerAnswers(String category, List<AnswerDto> answerDtos, Document document) {
        answerDtos.forEach(v->{
            answerRepository.save(v.toEntity(document, category));
        });
    }

    public TemplateDto.Response viewTemplate(Long documentId) {
        Document document = documentRepository.findDocumentWithBlocks(documentId);
        List<Block> blocks = document.getBlocks(); // template에 저장된 모든 블럭
        Map<String, List<Block>> categorizedBlocks = new HashMap<>();
        blocks.forEach(v->{
            String category = v.getCategory();
            categorizedBlocks.computeIfAbsent(category, k-> new ArrayList<>()).add(v);
        });
        List<TemplateCategoryDto> categoryDtos = new ArrayList<>();
        for (Map.Entry<String, List<Block>> entry : categorizedBlocks.entrySet()) {
            String category = entry.getKey();
            List<Block> blocksInCategory = entry.getValue();
            List<BlockDto> blockDtos = new ArrayList<>();
            blocksInCategory.forEach(v->{
                blockDtos.add(v.toDto());
            });
            categoryDtos.add(TemplateCategoryDto.builder()
                    .categoryType(category)
                    .blockDtos(blockDtos).build());
        }
        return TemplateDto.Response.builder()
                .templateId(documentId)
                .userId(document.getUser().getId())
                .title(document.getTitle())
                .categoryDtos(categoryDtos).build();
    }

    public DocumentDto.Response viewDocument(Long documentId) {
        Document document = documentRepository.findDocumentWithAnswers(documentId);
        List<Answer> answers = document.getAnswers(); // template에 저장된 모든 블럭
        Map<String, List<Answer>> categorizedAnswers = new HashMap<>();
        answers.forEach(v->{
            String category = v.getCategory();
            categorizedAnswers.computeIfAbsent(category, k-> new ArrayList<>()).add(v);
        });
        List<DocumentCategoryDto> categoryDtos = new ArrayList<>();
        for (Map.Entry<String, List<Answer>> entry : categorizedAnswers.entrySet()) {
            String category = entry.getKey();
            List<Answer> answersInCategory = entry.getValue();
            List<AnswerDto> answerDtos = new ArrayList<>();
            answersInCategory.forEach(v->{
                String type = v.getDtype();
                answerDtos.add(v.toEntity());
            });
            categoryDtos.add(DocumentCategoryDto.builder()
                    .categoryType(category)
                    .answerDtos(answerDtos).build());
        }
        return DocumentDto.Response.builder()
                .documentId(documentId)
                .title(document.getTitle())
                .userId(document.getUser().getId())
                .writer(document.getWriter())
                .categoryDtos(categoryDtos).build();
    }

    public List<DocumentDto.Response> viewAllDocument(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("존재하지 않는 사용자입니다."));
        List<Document> documents = user.getDocuments();
        List<DocumentDto.Response> documentDtos = new ArrayList<>();
        documents.forEach(v->{
            if(v.getWriter()!=null){
                documentDtos.add(viewDocument(v.getId()));
            }
        });
        return documentDtos;
    }

    public String deleteTemplate(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("존재하지 않는 템플릿입니다."));
        documentRepository.delete(document);
        return "SUCCESS";
    }
}
