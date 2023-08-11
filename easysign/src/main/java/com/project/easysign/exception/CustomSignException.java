package com.project.easysign.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class CustomSignException extends RuntimeException{
    public final Map<String, String> validation = new HashMap<>();

    public CustomSignException(String message){
        super(message);
    }
    public CustomSignException(String message, Throwable cause){
        super(message, cause);
    }
    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message){
        validation.put(fieldName, message);
    }
}
