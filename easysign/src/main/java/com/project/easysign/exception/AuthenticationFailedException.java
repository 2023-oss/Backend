package com.project.easysign.exception;

public class AuthenticationFailedException extends CustomSignException {
    @Override
    public int getStatusCode() {
        return 400;
    }

    public AuthenticationFailedException(String message){
        super(message);
    }
}
