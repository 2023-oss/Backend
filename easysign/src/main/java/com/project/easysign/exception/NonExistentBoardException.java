package com.project.easysign.exception;

public class NonExistentBoardException extends CustomSignException {
    private static final String MESSAGE = "존재하지 않는 게시글입니다.";
    public NonExistentBoardException(){
        super(MESSAGE);
    }
    @Override
    public int getStatusCode() {
        return 400;
    }
}
