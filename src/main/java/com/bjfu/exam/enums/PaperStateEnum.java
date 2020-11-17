package com.bjfu.exam.enums;

import lombok.Getter;

@Getter
public enum PaperStateEnum {
    CREATING(1, "创建中"),
    ANSWERING(2, "作答中"),
    END_ANSWER(3, "结束作答"),
    READY_TO_ANSWERING(4, "准备开始作答");

    private final int state;
    private final String msg;

    PaperStateEnum(int state, String msg) {
        this.state = state;
        this.msg = msg;
    }
}
