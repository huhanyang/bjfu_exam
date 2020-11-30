package com.bjfu.exam.enums;

import lombok.Getter;

@Getter
public enum UserStateEnum {

    ACTIVE(1, "活跃状态"),
    BANNED(2, "封禁状态");

    private final int type;
    private final String msg;

    UserStateEnum(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }
}
