package com.bjfu.exam.exception;

public class NotAllowOperationException extends RuntimeException{

    public NotAllowOperationException(String message) {
        super(message);
    }

}
