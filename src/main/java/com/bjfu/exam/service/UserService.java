package com.bjfu.exam.service;

import com.bjfu.exam.dto.UserDTO;
import com.bjfu.exam.request.UserChangePasswordRequest;
import com.bjfu.exam.request.UserRegisterRequest;

public interface UserService {
    /**
     * 登录检查
     */
    UserDTO loginCheck(String account, String password);
    /**
     * 注册
     */
    UserDTO register(UserRegisterRequest userRegisterRequest);
    /**
     * 修改密码
     */
    UserDTO changePassword(UserChangePasswordRequest userChangePasswordRequest);
}
