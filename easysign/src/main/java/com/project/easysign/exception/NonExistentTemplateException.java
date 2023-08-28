package com.project.easysign.exception;

public class NonExistentTemplateException extends CustomSignException {
    private static final String MESSAGE = "존재하지 않는 동의서 양식 입니다.";
    public NonExistentTemplateException(){
        super(MESSAGE);
    }
    @Override
    public int getStatusCode() {
        return 400;
    }
}
