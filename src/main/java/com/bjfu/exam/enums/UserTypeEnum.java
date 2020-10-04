package com.bjfu.exam.enums;

import lombok.Getter;

@Getter
public enum UserTypeEnum {
    TEACHER(1, "教师"),
    STUDENT(2, "学生"),
    ADMIN(3, "管理员");

    private final int type;
    private final String msg;

    UserTypeEnum(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }
}
