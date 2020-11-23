package com.bjfu.exam.enums;

import lombok.Getter;

@Getter
public enum ProblemTypeEnum {

    CHOICE_PROBLEM(1, "选择题"),
    MATERIAL_PROBLEM(2, "材料题"),
    FATHER_PROBLEM(3, "复合题目");

    private final int type;
    private final String msg;

    ProblemTypeEnum(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }
}
