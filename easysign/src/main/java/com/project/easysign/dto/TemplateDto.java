package com.project.easysign.dto;

import com.project.easysign.domain.Document;
import com.project.easysign.domain.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;


public class TemplateDto {
    @Data
    @Builder
    public static class Request{
        private Long userId;
        private String title;
        private List<TemplateCategoryDto> categoryDtos;
        public Document toEntity(User user){
            return Document.builder()
                    .user(user)
                    .title(this.title)
                    .build();
        }
    }
    @Data
    @Builder
    public static class Response{
        private Long templateId;
        private Long userId;
        private String title;
        private List<TemplateCategoryDto> categoryDtos;
    }
}
