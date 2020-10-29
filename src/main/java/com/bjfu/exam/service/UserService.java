package com.bjfu.exam.service;

import com.bjfu.exam.dto.user.UserDTO;
import com.bjfu.exam.dto.user.UserDetailDTO;
import com.bjfu.exam.request.user.LoginRequest;
import com.bjfu.exam.request.user.UserChangePasswordRequest;
import com.bjfu.exam.request.user.UserRegisterRequest;

/**
 * 用户相关
 */
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
     * 获取用户详情信息
     */
    UserDetailDTO getUserDetail(Long id);
    /**
     * 获取用户的信息
     */
    UserDTO getUserInfo(Long id);
}
