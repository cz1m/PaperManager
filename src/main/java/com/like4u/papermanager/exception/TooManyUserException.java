package com.like4u.papermanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN,reason = "人太多啦")
public class TooManyUserException extends RuntimeException{
    public TooManyUserException(){}

    public  TooManyUserException(String msg){
        super(msg);

    }
}
