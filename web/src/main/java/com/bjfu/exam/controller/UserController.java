package com.bjfu.exam.controller;

import com.bjfu.exam.core.ao.UserAO;
import com.bjfu.exam.core.dto.user.UserDTO;
import com.bjfu.exam.core.params.user.UserChangePasswordParams;
import com.bjfu.exam.core.params.user.UserLoginParams;
import com.bjfu.exam.core.params.user.UserRegisterParams;
import com.bjfu.exam.utils.DTOConvertToVOUtil;
import com.bjfu.exam.utils.SessionUtil;
import com.bjfu.exam.vo.BaseResult;
import com.bjfu.exam.vo.user.UserVO;
import com.bjfu.exam.api.enums.ResultEnum;
import com.bjfu.exam.api.enums.SessionKeyEnum;
import com.bjfu.exam.api.enums.UserStateEnum;
import com.bjfu.exam.api.enums.UserTypeEnum;
import com.bjfu.exam.request.user.UserLoginRequest;
import com.bjfu.exam.request.user.UserChangePasswordRequest;
import com.bjfu.exam.request.user.UserRegisterRequest;
import com.bjfu.exam.interceptor.annotation.HttpAuthCheck;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * 用户相关http接口
 * @author warthog
 */
@Validated
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserAO userAO;

    @HttpAuthCheck(needLogin = false)
    @PostMapping("/loginCheck")
    public BaseResult<UserVO> loginCheck(@Validated @RequestBody UserLoginRequest loginRequest, HttpSession session) {
        UserLoginParams userLoginParams = new UserLoginParams();
        BeanUtils.copyProperties(loginRequest, userLoginParams);

        UserDTO userDTO = userAO.loginCheck(userLoginParams);
        SessionUtil.initSession(session, userDTO);
        UserVO userDetailVO = DTOConvertToVOUtil.convertUserDTO(userDTO);
        return new BaseResult<>(ResultEnum.SUCCESS, userDetailVO);
    }

    @HttpAuthCheck(needLogin = false)
    @PostMapping("/register")
    public BaseResult<UserVO> register(@Validated @RequestBody UserRegisterRequest userRegisterRequest,
                                       HttpSession session) {
        UserRegisterParams userRegisterParams = new UserRegisterParams();
        BeanUtils.copyProperties(userRegisterRequest, userRegisterParams);
        userRegisterParams.setType(UserTypeEnum.STUDENT);
        userRegisterParams.setState(UserStateEnum.ACTIVE);

        UserDTO userDTO = userAO.register(userRegisterParams);
        SessionUtil.initSession(session, userDTO);
        UserVO userDetailVO = DTOConvertToVOUtil.convertUserDTO(userDTO);
        return new BaseResult<>(ResultEnum.SUCCESS, userDetailVO);
    }

    @HttpAuthCheck(needLogin = false)
    @PostMapping("/changePassword")
    public BaseResult<Void> changePassword(@Validated @RequestBody UserChangePasswordRequest userChangePasswordRequest,
                                           HttpSession session) {
        UserChangePasswordParams userChangePasswordParams = new UserChangePasswordParams();
        BeanUtils.copyProperties(userChangePasswordRequest, userChangePasswordParams);

        userAO.changePassword(userChangePasswordParams);
        SessionUtil.deleteSession(session);
        return new BaseResult<>(ResultEnum.SUCCESS);
    }

    @HttpAuthCheck
    @GetMapping("/getUserInfo")
    public BaseResult<UserVO> getUserInfo(HttpSession session) {
        UserDTO userInfo = userAO.getUserInfo((String) session.getAttribute(SessionKeyEnum.LOGIN_USER_ID.name()));
        if(userInfo.getState().equals(UserStateEnum.BANNED)) {
            SessionUtil.deleteSession(session);
            return new BaseResult<>(ResultEnum.ACCOUNT_IS_BANNED);
        }
        UserVO userVO = DTOConvertToVOUtil.convertUserDTO(userInfo);
        return new BaseResult<>(ResultEnum.SUCCESS, userVO);
    }

}
