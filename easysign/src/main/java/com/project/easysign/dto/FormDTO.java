package com.project.easysign.dto;

import com.project.easysign.domain.Form;
import lombok.Builder;
import lombok.Data;


public class FormDTO {
    @Data
    public static class Request{
        private String vp;
        private String form;
    }
    @Data
    public static class Response{
        private Long id;
        private String vpId;
        private String form;
        private String createdDate;

        public Response(Form form) {
            this.id = form.getId();
            this.vpId = form.getVpId();
            this.form = form.getForm();
            this.createdDate = form.getCreatedDate().toString();
        }
    }


}
