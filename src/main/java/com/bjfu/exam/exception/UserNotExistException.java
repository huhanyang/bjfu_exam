package com.bjfu.exam.exception;

import lombok.Getter;

@Getter
public class UserNotExistException extends RuntimeException {

    private final String userId;
    private final String userAccount;
    private final String message;

    public UserNotExistException(String userAccount, String message) {
        super(message);
        this.userId = null;
        this.userAccount = userAccount;
        this.message = message;
    }

    public UserNotExistException(Long userId, String message) {
        super(message);
        this.userId = userId.toString();
        this.userAccount = null;
        this.message = message;
    }

}
