package com.project.easysign.dto;

import com.project.easysign.domain.Block;
import com.project.easysign.domain.Document;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlockDto {
    private String question;
    private String answerType;
    public Block toEntity(Document document, String category){
        return Block.builder()
                .document(document)
                .category(category)
                .question(question)
                .answerType(answerType).build();
    }
}
