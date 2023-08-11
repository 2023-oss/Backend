package com.project.easysign.exception;

public class NonExistentStoreException extends CustomSignException {
    private static final String MESSAGE = "존재하지 않는 점포입니다.";
    public NonExistentStoreException(){
        super(MESSAGE);
    }
    @Override
    public int getStatusCode() {
        return 400;
    }
}
