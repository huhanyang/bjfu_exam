package com.bjfu.exam.api.enums;

import lombok.Getter;

@Getter
public enum UserTypeEnum {

    TEACHER("教师"),
    STUDENT("学生"),
    ADMIN("管理员");

    private final String msg;

    UserTypeEnum(String msg) {
        this.msg = msg;
    }
}
