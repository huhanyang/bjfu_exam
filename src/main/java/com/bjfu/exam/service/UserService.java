package com.bjfu.exam.service;

import com.bjfu.exam.dto.user.UserDTO;
import com.bjfu.exam.dto.user.UserDetailDTO;
import com.bjfu.exam.request.LoginRequest;
import com.bjfu.exam.request.UserChangePasswordRequest;
import com.bjfu.exam.request.UserRegisterRequest;

public interface UserService {
    /**
     * 登录检查
     */
    UserDetailDTO loginCheck(LoginRequest request);
    /**
     * 注册
     */
    UserDetailDTO register(UserRegisterRequest userRegisterRequest);
    /**
     * 修改密码
     */
    UserDTO changePassword(UserChangePasswordRequest userChangePasswordRequest);
    /**
     * 获取用户详情
     */
    UserDetailDTO getUserDetail(Long id);
}
