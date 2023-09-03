package com.project.easysign.exception;

public class NonExistentException extends CustomSignException {
    public NonExistentException(String message){
        super(message);
    }
    @Override
    public int getStatusCode() {
        return 400;
    }
}
