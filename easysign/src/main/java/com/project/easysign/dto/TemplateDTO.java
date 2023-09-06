package com.project.easysign.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TemplateDTO {
    public String templateId;
    public String company;
    public String picture;
    public String jsonData;
}
