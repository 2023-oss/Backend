package com.project.easysign.dto;

import com.project.easysign.domain.Role;
import com.project.easysign.domain.User;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UserDTO {
    @Data
    public static class Request{
        @NotBlank(message="아이디는 필수 입력값입니다.")
        @Pattern(regexp = "^[a-z0-8]{4,20}$",message = "아이디는 영어 소문자와 숫자만 사용하여 4~20자리여야 합니다.")
        private String userId;
        @NotBlank(message = "패스워드는 필수 입력 값입니다.")
        @Pattern(regexp="(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{6,12}",
                message = "비밀번호는 영문자와 숫자, 특수기호가 적어도 1개 이상 포함된 6자~12자의 비밀번호여야 합니다.")
        private String pw;
        @NotBlank(message = "이름은 필수 입력값입니다.")
        private String userName;
        private String company;
        private String companyType;
        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Email(message = "이메일 형식에 맞지 않습니다.")
        private String email;

        public User toEntity(String imgUrl, Role role, String pass) {
            return User.builder()
                    .userId(userId)
                    .pw(pass)
                    .userName(userName)
                    .email(email)
                    .company(company)
                    .companyType(companyType)
                    .picture(imgUrl)
                    .role(role).build();
        }
    }
}
