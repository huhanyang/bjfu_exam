package com.bjfu.exam.core.ao;

import com.bjfu.exam.api.bo.Page;
import com.bjfu.exam.core.exception.BizException;
import com.bjfu.exam.core.params.user.*;
import com.bjfu.exam.core.dto.user.UserDTO;

public interface UserAO {
    /**
     * 登录检查
     *
     * @param params 参数
     * @return 登录成功的用户信息
     * @exception BizException ACCOUNT_NOT_EXIST_OR_PASSWORD_ERROR 账号密码错误
     * @exception BizException ACCOUNT_IS_BANNED 账号被封禁
     */
    UserDTO loginCheck(UserLoginParams params);

    /**
     * 注册
     *
     * @param params 参数
     * @return 注册成功的用户信息
     * @exception BizException ACCOUNT_EXIST 账号重复
     */
    UserDTO register(UserRegisterParams params);

    /**
     * 修改密码
     *
     * @param params 参数
     * @return 修改成功的用户信息
     * @exception BizException ACCOUNT_NOT_EXIST_OR_PASSWORD_ERROR 账号密码错误
     * @exception BizException ACCOUNT_IS_BANNED 账号被封禁
     */
    UserDTO changePassword(UserChangePasswordParams params);

    /**
     * 获取用户的信息
     *
     * @param account 用户账号
     * @return 用户信息
     * @exception BizException ACCOUNT_NOT_EXIST 账号不存在
     */
    UserDTO getUserInfo(String account);

    /**
     * 获取用户的信息
     *
     * @param userId 用户Id
     * @return 用户信息
     * @exception BizException ACCOUNT_NOT_EXIST 账号不存在
     */
    UserDTO getUserInfo(Long userId);

    /**
     * 获取教师账号列表
     *
     * @param params 参数
     * @return 拉取到的用户
     */
    Page<UserDTO> listUsersByType(UserListUsersParams params);

    /**
     * 修改用户的状态
     *
     * @param params 参数
     * @return 修改后的用户信息
     * @exception BizException ACCOUNT_NOT_EXIST 账号不存在
     * @exception BizException CANT_CHANGE_ADMIN_STATE 禁止修改管理员的状态
     */
    UserDTO changeUserState(UserChangeUserStateParams params);
}
