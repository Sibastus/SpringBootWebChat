package com.chernyshov.exceptions;

public class IncorrectParameterException extends RuntimeException {
    public IncorrectParameterException(String message){
        super(message);
    }
}
