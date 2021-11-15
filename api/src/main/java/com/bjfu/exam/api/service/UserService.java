package com.bjfu.exam.api.service;

import com.bjfu.exam.core.dto.user.UserDTO;
import com.bjfu.exam.api.enums.UserStateEnum;
import com.bjfu.exam.api.enums.UserTypeEnum;
import com.bjfu.exam.core.params.user.*;

/**
 * 用户相关服务
 * @author warthog
 */
public interface UserService {
    /**
     * 登录检查
     *
     * @param request 请求
     * @return 登录成功的用户信息
     * @exception BizException ACCOUNT_NOT_EXIST_OR_PASSWORD_ERROR 账号密码错误
     * @exception BizException ACCOUNT_IS_BANNED 账号被封禁
     */
    UserDTO loginCheck(UserLoginParams request);

    /**
     * 注册
     *
     * @param request 请求
     * @param type 要注册的账号类型
     * @param state 要注册的账号状态
     * @return 注册成功的用户信息
     * @exception BizException ACCOUNT_EXIST 账号重复
     */
    UserDTO register(UserRegisterParams request, UserTypeEnum type, UserStateEnum state);

    /**
     * 修改密码
     *
     * @param request 请求
     * @return 修改成功的用户信息
     * @exception BizException ACCOUNT_NOT_EXIST_OR_PASSWORD_ERROR 账号密码错误
     * @exception BizException ACCOUNT_IS_BANNED 账号被封禁
     */
    UserDTO changePassword(UserChangePasswordParams request);

    /**
     * 获取用户的信息
     *
     * @param account 用户账号
     * @return 用户信息
     * @exception BizException ACCOUNT_NOT_EXIST 账号不存在
     */
    UserDTO getUserInfo(String account);

    /**
     * 获取教师账号列表
     *
     * @param request 分页请求
     * @param type 账号类型
     * @return 拉取到的用户
     */
    Page<UserDTO> listUsersByType(UserListUsersParams request, UserTypeEnum type);

    /**
     * 修改用户的状态
     * @param userAccount 用户账号
     * @param newState 用户新状态
     * @return 修改后的用户信息
     * @exception BizException ACCOUNT_NOT_EXIST 账号不存在
     * @exception BizException CANT_CHANGE_ADMIN_STATE 禁止修改管理员的状态
     */
    UserDTO changeUserState(UserChangeUserStateParams request);
}
