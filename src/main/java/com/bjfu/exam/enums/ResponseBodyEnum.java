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
    CHANGE_PASSWORD_FAILED(7, "修改密码失败，账号或密码错误"),
    PARAM_NOT_MATCH(8, "参数不相互匹配"),
    ANSWER_TWICE(9, "多次作答"),
    PAPER_STATE_NOT_ANSWERING(10, "试卷非可作答状态"),
    ANSWER_OTHERS_PAPER(11, "作答他人试卷"),
    NOT_CONTINUOUS_ANSWERING(12, "非连续答题"),
    USER_NOT_EXIST(13, "用户不存在"),
    PAPER_NOT_EXIST(14, "试卷不存在"),
    POLYMERIZATION_PROBLEM_NOT_EXIST(15, "组合题目不存在"),
    PROBLEM_NOT_EXIST(16, "题目不存在"),
    PAPER_ANSWER_NOT_EXIST(17, "答卷不存在"),
    NOT_TEACHER_CREATE_PAPER(18, "非教师用户创建试卷"),
    NOT_CREATOR_EDIT_PAPER(19, "非试卷创建者编辑试卷"),
    DATA_WRONG(20, "数据错误，如仍然错误请联系管理员"),
    NEW_SORT_PARAM_WRONG(21, "排序参数错误"),
    CREATE_NOT_ALLOW_ACCOUNT_TYPE(22, "创建不允许的账号类型"),
    GET_OTHERS_PAPER_ANSWER(23, "获取他人试卷"),
    PAPER_STATE_FROM_ANSWERING_TO_CREATING(24, "试卷状态不允许从作答修改回创建状态"),
    PAPER_STATE_FROM_DELETE_TO_OTHERS(25, "试卷状态不允许从删除状态修改为其他状态"),
    NOT_CREATOR_EXPORT_PAPER(26, "非试卷创建者导出试卷"),
    EXPORT_PAPER_FAILED(27, "导出excel失败");


    private final int code;
    private final String msg;

    ResponseBodyEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
