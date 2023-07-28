package com.project.easysign.domain.dto;

import com.project.easysign.domain.Document;
import com.project.easysign.domain.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class DocumentDto {
    @Data
    @Builder
    public static class Request{
        private String title;
        private Long userId;
        private String writer;
        private List<DocumentCategoryDto> categoryDtos;
        public Document toEntity(User user){
            return Document.builder()
                    .user(user)
                    .title(title)
                    .writer(writer)
                    .build();
        }
    }

    @Data
    @Builder
    public static class Response{
        private Long documentId;
        private String title;
        private Long userId;
        private String writer;
        private List<DocumentCategoryDto> categoryDtos;
    }
}
