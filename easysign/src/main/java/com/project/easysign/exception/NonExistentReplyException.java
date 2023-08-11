package com.project.easysign.exception;

public class NonExistentReplyException extends CustomSignException {
    private static final String MESSAGE = "존재하지 않는 댓글입니다.";
    public NonExistentReplyException(){
        super(MESSAGE);
    }
    @Override
    public int getStatusCode() {
        return 400;
    }
}
