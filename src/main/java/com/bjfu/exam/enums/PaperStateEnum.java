package com.bjfu.exam.enums;

import lombok.Getter;

@Getter
public enum PaperStateEnum {
    CREATING(1, "创建中"),
    READY_TO_ANSWERING(2, "准备开始作答"),
    ANSWERING(3, "作答中"),
    END_ANSWER(4, "结束作答"),
    SOFT_DELETE(5, "软删除");

    private final int state;
    private final String msg;

    PaperStateEnum(int state, String msg) {
        this.state = state;
        this.msg = msg;
    }
}
