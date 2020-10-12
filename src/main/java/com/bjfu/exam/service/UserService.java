package com.bjfu.exam.service;

import com.bjfu.exam.dto.user.UserDTO;
import com.bjfu.exam.dto.user.UserDetailDTO;
import com.bjfu.exam.request.user.LoginRequest;
import com.bjfu.exam.request.user.UserChangePasswordRequest;
import com.bjfu.exam.request.user.UserRegisterRequest;

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
