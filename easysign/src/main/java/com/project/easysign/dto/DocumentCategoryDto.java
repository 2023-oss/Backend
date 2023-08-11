package com.project.easysign.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class DocumentCategoryDto {

    private String categoryType;
    private List<AnswerDto> answerDtos;
}
