package com.bjfu.exam.exception;

import lombok.Getter;

@Getter
public class UnauthorizedOperationException extends RuntimeException{

    private final String userId;
    private final String message;

    public UnauthorizedOperationException(Long userId, String message) {
        super(message);
        this.userId = userId.toString();
        this.message = message;
    }

}
