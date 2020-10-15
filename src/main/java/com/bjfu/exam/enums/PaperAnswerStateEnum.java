package com.bjfu.exam.enums;

import lombok.Getter;

@Getter
public enum PaperAnswerStateEnum {

    ANSWERING(1, "作答中"),
    FINISH(2, "作答完成"),
    OVERTIME(3, "作答时间超时");

    private final int state;
    private final String msg;

    PaperAnswerStateEnum(int state, String msg) {
        this.state = state;
        this.msg = msg;
    }
}
