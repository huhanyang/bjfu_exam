package com.bjfu.exam.enums;

import lombok.Getter;

@Getter
public enum ResponseBodyEnum {

    UNKNOWN_WRONG(0, "未知的错误，请重新登陆后再试"),
    SUCCESS(1, "成功"),
    PARAM_WRONG(2, "参数错误"),
    LOGIN_FAILED(3, "登录失败，账号或密码错误"),
    ACCOUNT_RECUR(4, "账号重复"),
    FIND_FAILED(5, "查找失败"),
    NEED_TO_RELOGIN(6, "需要重新登录"),
    CHANGE_PASSWORD_FAILED(7, "修改密码失败，账号或密码错误");


    private final int code;
    private final String msg;

    ResponseBodyEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
