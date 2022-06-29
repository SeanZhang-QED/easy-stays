package com.easy.easystays.exception;

public class UserAlreadyExistException extends RuntimeException{
    public UserAlreadyExistException(String msg) {
        super(msg);
    }
}
