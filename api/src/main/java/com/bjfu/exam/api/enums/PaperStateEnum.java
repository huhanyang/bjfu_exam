package com.bjfu.exam.api.enums;

import lombok.Getter;

@Getter
public enum PaperStateEnum {

    CREATING("创建中"),
    READY_TO_ANSWERING("准备开始作答"),
    ANSWERING("作答中"),
    END_ANSWER("结束作答"),
    SOFT_DELETE("软删除");

    private final String msg;

    PaperStateEnum(String msg) {
        this.msg = msg;
    }
}
