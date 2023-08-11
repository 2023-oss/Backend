package com.project.easysign.dto;

import com.project.easysign.domain.Answer;
import com.project.easysign.domain.Document;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnswerDto {
    private String question;
    private String answerType;
    private Object answer;

    public Answer toEntity(Document document, String category) {
        if(answerType.equals("BOOL")){
            return Answer.builder()
                    .document(document)
                    .category(category)
                    .dtype(answerType)
                    .question(question)
                    .bool_ans((Boolean) answer).build();
        }else if(answerType.equals("TEXT")){
            return Answer.builder()
                    .document(document)
                    .category(category)
                    .dtype(answerType)
                    .question(question)
                    .text_ans((String) answer).build();
        }else if(answerType.equals("SIGN")){
            return Answer.builder()
                    .document(document)
                    .category(category)
                    .dtype(answerType)
                    .question(question)
                    .sign_ans((String) answer).build();
        }
        return null;
    }
}
