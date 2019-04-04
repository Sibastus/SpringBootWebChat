package com.chernyshov.exceptions;

public class UserAlreadyBanedException extends RuntimeException {
    public UserAlreadyBanedException(String message){
        super(message);
    }
}
