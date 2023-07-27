package com.project.easysign.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore // 양방향 매핑 시 JsonIgnore를 추가해 무한루프 해결
    @OneToMany(mappedBy = "document", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Category> categories = new ArrayList<>();

    private String title;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private Long userId;
        private String title;
        private List<Category.Request> categories;
        /* Dto -> Entity */
//        public Reply toEntity(User user, Board board) {
//            Reply reply = Reply.builder()
//                    .user(user)
//                    .board(board)
//                    .commentContent(comment)
//                    .build();
//            return reply;
//        }
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        private String company;
        private String title;
        private List<Category.Response> categoryList;

    }
}
