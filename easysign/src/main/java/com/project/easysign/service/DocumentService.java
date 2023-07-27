package com.project.easysign.service;

import com.project.easysign.domain.Block;
import com.project.easysign.domain.Category;
import com.project.easysign.domain.Document;
import com.project.easysign.domain.User;
import com.project.easysign.domain.answer.Answer;
import com.project.easysign.domain.answer.BoolAnswer;
import com.project.easysign.repository.BlockRepository;
import com.project.easysign.repository.CategoryRepository;
import com.project.easysign.repository.DocumentRepository;
import com.project.easysign.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class DocumentService {
    private DocumentRepository documentRepository;
    private CategoryRepository categoryRepository;
    private BlockRepository blockRepository;
    private UserRepository userRepository;

    public String register(Document.Request request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(()-> new RuntimeException("해당 사용자가 존재하지 않습니다."));
        // 1. Document 저장
        Document document = documentRepository.save(Document.builder()
                        .title(request.getTitle())
                        .user(user)
                        .build());
        List<Category.Request> categorylist = request.getCategories();
        // 2. Category 저장
        saveCategories(document, categorylist);

        return "SUCCESS";
    }

    public void saveCategories(Document document, List<Category.Request> categorylist){
        categorylist.forEach(v->{
            Category category = categoryRepository.save(Category.builder()
                    .categoryType(v.getCategoryType())
                    .document(document).build());
            List<Block.Request> blockList = v.getBlocklist();
            // 3. Block 저장
            saveBlocks(category, blockList);
        });
    }
    public void saveBlocks(Category category, List<Block.Request> blockList){
        blockList.forEach(v->{
            Block block = blockRepository.save(Block.builder()
                    .category(category)
                    .question(v.getQuestion())
                    .answerType(v.getAnswerType()).build());
        });
    }

    public void view(Long documentId) {
        Document document = documentRepository.findDocumentWithCategoriesAndBlocks(documentId);
        log.info("성공?");
        log.info("document categories = {}",document.getCategories());

    }
}
