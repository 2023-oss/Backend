package com.project.easysign.dto;

import com.project.easysign.domain.Form;
import lombok.Builder;
import lombok.Data;


public class FormDTO {
    @Data
    public static class Request{
        private String name;
        private String phone;
    }
    @Data
    public static class Response{
        private Long id;
        private String name;
        private String phone;
        private String createdDate;

        public Response(Form form) {
            this.id = form.getId();
            this.name = form.getName();
            this.phone = form.getPhone();
            this.createdDate = form.getCreatedDate().toString();
        }
    }

}
