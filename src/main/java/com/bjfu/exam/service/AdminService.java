package com.bjfu.exam.service;

import com.bjfu.exam.dto.user.UserDTO;
import com.bjfu.exam.request.admin.AdminCreateTeacherAccountRequest;

/**
 * 管理员相关
 */
public interface AdminService {
    /**
     * 管理员创建教师账号
     */
    UserDTO createTeacherAccount(Long userId, AdminCreateTeacherAccountRequest adminCreateTeacherAccountRequest);
}
