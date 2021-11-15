package com.bjfu.exam.api.enums;

import lombok.Getter;

@Getter
public enum ProblemTypeEnum {

    CHOICE_PROBLEM("选择题"),
    MATERIAL_PROBLEM("材料题"),
    FATHER_PROBLEM("复合题目");

    private final String msg;

    ProblemTypeEnum(String msg) {
        this.msg = msg;
    }
}
