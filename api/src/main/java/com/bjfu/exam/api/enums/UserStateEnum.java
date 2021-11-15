package com.bjfu.exam.api.enums;

import lombok.Getter;

@Getter
public enum UserStateEnum {

    ACTIVE("活跃状态"),
    BANNED("封禁状态");

    private final String msg;

    UserStateEnum(String msg) {
        this.msg = msg;
    }
}
