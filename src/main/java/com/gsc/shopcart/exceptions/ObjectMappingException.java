package com.gsc.shopcart.exceptions;

public class ObjectMappingException extends RuntimeException {

    public ObjectMappingException(String message){
        super(message);

    }

    public ObjectMappingException(String message, Throwable throwable){
        super(message, throwable);

    }
}
