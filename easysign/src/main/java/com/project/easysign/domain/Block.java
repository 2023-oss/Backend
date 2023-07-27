package com.project.easysign.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.easysign.domain.answer.Answer;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity(name = "block")
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    private String question;
    private String answerType;
    @JsonIgnore
    @OneToMany(mappedBy = "block", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Answer> answers;

    public Block.Response toDto(){
        Block.Response res = Response.builder()
                .question(question)
                .answerType(answerType).build();
        return res;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private String question;
        private String answerType;
//        public Reply toEntity(User user, Board board) {
//            Reply reply = Reply.builder()
//                    .user(user)
//                    .board(board)
//                    .commentContent(comment)
//                    .build();
//            return reply;
        /* Dto -> Entity */
//        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        private String question;
        private String answerType;
    }

}
