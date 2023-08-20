package com.project.easysign.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorizationDTO {
    @NotBlank(message = "아이디를 입력하세요")
    private String username;
    @NotBlank(message = "패스워드를 입력하세요")
    private String password;
}
