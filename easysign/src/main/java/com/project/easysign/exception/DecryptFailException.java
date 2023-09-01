package com.project.easysign.exception;

public class DecryptFailException extends CustomSignException{
    private static final String MESSAGE = "복호화에 실패했습니다.";
    public DecryptFailException(){super(MESSAGE);}
    @Override
    public int getStatusCode() {
        return 0;
    }
}
