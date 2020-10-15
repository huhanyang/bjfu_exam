package com.bjfu.exam.enums;

import lombok.Getter;

@Getter
public enum PaperStateEnum {
    CREATING(1, "创建中"),
    ANSWERING(2, "作答中"),
    DELETE(3, "软删除");

    private final int state;
    private final String msg;

    PaperStateEnum(int state, String msg) {
        this.state = state;
        this.msg = msg;
    }
}
