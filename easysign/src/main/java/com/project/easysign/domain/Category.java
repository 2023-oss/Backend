package com.project.easysign.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private Document document;
    private String categoryType;
    @JsonIgnore
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Block> blocks = new ArrayList<>();

    public Category.Response toDTO(){
        Category.Response res = Response.builder()
                .categoryType(categoryType)
                .blocks(blocks)
                .build();
        return res;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        private String categoryType;
        private List<Block.Request> blocklist;
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
        private String categoryType;
        private List<Block> blocks;
        /* Entity -> Dto*/

    }
}
