package com.project.easysign.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TemplateCategoryDto {
    private String categoryType;
    private List<BlockDto> blockDtos;
}
