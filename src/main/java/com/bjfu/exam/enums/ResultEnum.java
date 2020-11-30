package com.bjfu.exam.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {

    // 基本返回码
    SUCCESS(101, "成功"),
    PARAM_WRONG(102, "参数错误"),
    NEED_TO_LOGIN(103, "需要登录"),
    REQUIRE_STUDENT(104, "此请求只允许学生账号"),
    REQUIRE_TEACHER(105, "此请求只允许教师账号"),
    REQUIRE_ADMIN(106, "此请求只允许管理员账号"),
    ACCOUNT_IS_BANNED(107, "账号已被封禁"),

    // 用户操作返回码
    LOGIN_FAILED(201, "登录失败，账号或密码错误"),
    ACCOUNT_RECUR(202, "账号重复"),
    CHANGE_PASSWORD_FAILED(203, "修改密码失败，账号或密码错误"),

    // 业务异常返回码
    NOT_TEACHER_CREATE_PAPER(301, "非教师用户创建试卷"),
    NOT_PAPER_CREATOR(302, "非试卷创建者"),
    FIND_FAILED(303, "未找到"),
    PAPER_STATE_NOT_ANSWERING(304, "试卷非可作答状态"),
    ANSWER_TWICE(305, "多次作答"),
    ANSWER_OTHERS_PAPER(306, "作答他人试卷"),
    PAPER_NOT_EXIST(307, "试卷不存在"),
    PROBLEM_NOT_EXIST(308, "题目不存在"),
    PAPER_ANSWER_NOT_EXIST(309, "答卷不存在"),
    PAPER_STATE_CHANGE_NOT_ALLOW(310, "试卷不允许变更为此状态"),
    PAPER_STATE_IS_NOT_CREATING(311, "试卷非创建状态不允许修改"),
    PAPER_STATE_IS_NOT_END_ANSWER(312, "试卷非结束作答状态不允许导出"),
    PAPER_STATE_CAN_NOT_DELETE(313, "试卷此状态不允许删除"),
    NOT_CREATOR_EXPORT_PAPER(314, "非试卷创建者不允许导出试卷"),
    EXPORT_PAPER_FAILED(315, "导出excel失败"),
    TEACHER_NOT_EXIST(316, "此教师不存在"),

    // 系统错误返回码
    USER_NOT_EXIST(401, "用户不存在"),
    OSS_CLIENT_INIT_FAILED(402, "oss客户端初始化失败"),
    OSS_UPLOAD_FILE_FAILED(403, "上传文件出错");

    private final int code;
    private final String msg;

    ResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
