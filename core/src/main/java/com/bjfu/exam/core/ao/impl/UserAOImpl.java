package com.bjfu.exam.core.ao.impl;

import com.bjfu.exam.api.bo.Page;
import com.bjfu.exam.api.enums.ResultEnum;
import com.bjfu.exam.api.enums.UserStateEnum;
import com.bjfu.exam.api.enums.UserTypeEnum;
import com.bjfu.exam.core.ao.UserAO;
import com.bjfu.exam.core.dto.user.UserDTO;
import com.bjfu.exam.core.exception.BizException;
import com.bjfu.exam.core.params.user.*;
import com.bjfu.exam.core.util.ConvertUtil;
import com.bjfu.exam.core.util.EntityConvertToDTOUtil;
import com.bjfu.exam.dao.entity.user.User;
import com.bjfu.exam.dao.repository.user.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Validated
@Component
public class UserAOImpl implements UserAO {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDTO loginCheck(UserLoginParams params) {
        // 账号密码检查
        User loginUser = userRepository.findByAccount(params.getAccount())
                .filter(user -> user.getPassword().equals(params.getPassword()))
                .orElseThrow(() -> new BizException(ResultEnum.ACCOUNT_NOT_EXIST_OR_PASSWORD_ERROR));
        // 账号状态检查
        if (loginUser.getState().equals(UserStateEnum.BANNED)) {
            throw new BizException(ResultEnum.ACCOUNT_IS_BANNED);
        }
        // 返回登录的用户
        return EntityConvertToDTOUtil.convertUser(loginUser);
    }

    @Override
    public UserDTO register(UserRegisterParams params) {
        // 检查账号是否重复
        userRepository.findByAccountForUpdate(params.getAccount())
                .ifPresent(user -> {
                    throw new BizException(ResultEnum.ACCOUNT_EXIST);
                });
        // 落库新用户
        User user = new User();
        BeanUtils.copyProperties(params, user);
        user.setType(params.getType());
        user.setState(params.getState());
        user = userRepository.save(user);
        // 返回新建的用户
        return EntityConvertToDTOUtil.convertUser(user);
    }

    @Override
    public UserDTO changePassword(UserChangePasswordParams params) {
        // 账号密码检查
        User user = userRepository.findByAccount(params.getAccount())
                .filter(user1 -> user1.getPassword().equals(params.getOldPassword()))
                .orElseThrow(() -> new BizException(ResultEnum.ACCOUNT_NOT_EXIST_OR_PASSWORD_ERROR));
        // 账号状态检查
        if (user.getState().equals(UserStateEnum.BANNED)) {
            throw new BizException(ResultEnum.ACCOUNT_IS_BANNED);
        }
        // 更新密码并落库
        user.setPassword(params.getNewPassword());
        user = userRepository.save(user);
        // 返回更新后的用户
        return EntityConvertToDTOUtil.convertUser(user);
    }

    @Override
    public UserDTO getUserInfo(String account) {
        return userRepository.findByAccount(account)
                .map(EntityConvertToDTOUtil::convertUser)
                .orElseThrow(() -> new BizException(ResultEnum.ACCOUNT_NOT_EXIST));
    }

    @Override
    public UserDTO getUserInfo(Long userId) {
        return userRepository.findById(userId)
                .map(EntityConvertToDTOUtil::convertUser)
                .orElseThrow(() -> new BizException(ResultEnum.ACCOUNT_NOT_EXIST));
    }

    @Override
    public Page<UserDTO> listUsersByType(UserListUsersParams params) {
        PageRequest pageRequest = PageRequest.of(params.getPage(), params.getSize());
        org.springframework.data.domain.Page<User> all = userRepository.findAllByType(params.getType(), pageRequest);
        return ConvertUtil.convertJpaPageToExamPage(all, EntityConvertToDTOUtil::convertUser);
    }

    @Override
    public UserDTO changeUserState(UserChangeUserStateParams params) {
        // 检查用户是否存在
        User user = userRepository.findByAccount(params.getUserAccount())
                .orElseThrow(() -> new BizException(ResultEnum.ACCOUNT_NOT_EXIST));
        // 禁止修改管理员的状态
        if (user.getType().equals(UserTypeEnum.ADMIN)) {
            throw new BizException(ResultEnum.CANT_CHANGE_ADMIN_STATE);
        }
        // 更新用户状态并落库
        user.setState(params.getNewState());
        user = userRepository.save(user);
        // 返回更新后的用户信息
        return EntityConvertToDTOUtil.convertUser(user);
    }
}
