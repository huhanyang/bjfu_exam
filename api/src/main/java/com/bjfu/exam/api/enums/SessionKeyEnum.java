package com.bjfu.exam.api.enums;

import lombok.Getter;

/**
 * session所有的key
 */
@Getter
public enum SessionKeyEnum {

    LOGIN_USER_TYPE("用户账号类型"),
    LOGIN_USER_ID("用户id"),
    LOGIN_USER_ACCOUNT("用户账号");

    private final String msg;

    SessionKeyEnum(String msg) {
        this.msg = msg;
    }

}
