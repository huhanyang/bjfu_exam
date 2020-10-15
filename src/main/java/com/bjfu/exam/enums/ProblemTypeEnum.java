package com.bjfu.exam.enums;

import lombok.Getter;

@Getter
public enum ProblemTypeEnum {

    CHOICE_QUESTION(1, "选择题"),
    MATERIAL_QUESTION(2, "材料题");

    private final int type;
    private final String msg;

    ProblemTypeEnum(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }
}
