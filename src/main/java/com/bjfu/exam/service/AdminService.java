package com.bjfu.exam.service;

import com.bjfu.exam.dto.user.UserDTO;
import com.bjfu.exam.request.admin.AdminCreateTeacherAccountRequest;

import java.util.List;

/**
 * 管理员相关
 */
public interface AdminService {
    /**
     * 管理员创建教师账号
     */
    UserDTO createTeacherAccount(AdminCreateTeacherAccountRequest adminCreateTeacherAccountRequest);
    /**
     * 获取教师账号列表
     */
    List<UserDTO> getAllTeacherAccounts();
    /**
     * 封禁教师账号
     */
    void banTeacherAccount(Long teacherAccountId);
    /**
     * 解除封禁教师账号
     */
    void activeTeacherAccount(Long teacherAccountId);
}
