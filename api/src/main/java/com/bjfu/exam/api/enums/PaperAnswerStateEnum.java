package com.bjfu.exam.api.enums;

import lombok.Getter;

@Getter
public enum PaperAnswerStateEnum {

    ANSWERING("作答中"),
    FINISH("作答完成"),
    OVERTIME("作答时间超时");

    private final String msg;

    PaperAnswerStateEnum(String msg) {
        this.msg = msg;
    }
}
