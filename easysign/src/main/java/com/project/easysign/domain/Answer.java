package com.project.easysign.domain;

import com.project.easysign.dto.AnswerDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class Answer extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private Document document;
    private String category;
    private String dtype;
    private String question;
    private Boolean bool_ans;
    private String text_ans;
    private String sign_ans;

    public Answer(Document document, String category, String question, Boolean answer) {
        this.document = document;
        this.category = category;
        this.question = question;
        this.bool_ans = answer;
    }

    public AnswerDto toEntity() {
        if(dtype.equals("BOOL")){
            return AnswerDto.builder()
                    .question(question)
                    .answerType(dtype)
                    .answer(bool_ans).build();
        }else if(dtype.equals("TEXT")){
            return AnswerDto.builder()
                    .question(question)
                    .answerType(dtype)
                    .answer(text_ans).build();
        }else if(dtype.equals("SIGN")){
            return AnswerDto.builder()
                    .question(question)
                    .answerType(dtype)
                    .answer(sign_ans).build();
        }
        return null;
    }
}
