package com.project.easysign.exception;

public class SendMessageFailException extends CustomSignException {
    @Override
    public int getStatusCode() {
        return 400;
    }

    public SendMessageFailException(String message){
        super(message);
    }
}
