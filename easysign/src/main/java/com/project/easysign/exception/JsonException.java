package com.project.easysign.exception;

public class JsonException extends CustomSignException{
    public JsonException(String message){
        super(message);
    }
    @Override
    public int getStatusCode() {
        return 0;
    }
}
