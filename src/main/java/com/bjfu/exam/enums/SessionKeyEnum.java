package com.bjfu.exam.enums;

import lombok.Getter;

@Getter
public enum SessionKeyEnum {

    ACCOUNT_TYPE("type", "账号类型"),
    ACCOUNT_ID("userId", "用户账号id"),
    ACCOUNT_NUMBER("account", "用户账号");

    private final String key;
    private final String msg;

    SessionKeyEnum(String key, String msg) {
        this.key = key;
        this.msg = msg;
    }

}
