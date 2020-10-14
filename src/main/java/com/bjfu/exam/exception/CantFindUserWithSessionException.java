package com.bjfu.exam.exception;

import lombok.Getter;

@Getter
public class CantFindUserWithSessionException extends RuntimeException {

    private final String userIdInSession;

    public CantFindUserWithSessionException(Long userIdInSession) {
        super("session中的userid不能查找到user");
        this.userIdInSession = userIdInSession.toString();
    }
}
