package com.project.easysign.exception;

public class AlreadyExistsException extends CustomSignException {
    public AlreadyExistsException(String message){
        super(message);
    }
    @Override
    public int getStatusCode() {
        return 400;
    }
}
