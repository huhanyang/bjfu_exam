package com.bjfu.exam.controller;

import com.bjfu.exam.dto.user.UserDTO;
import com.bjfu.exam.enums.ResultEnum;
import com.bjfu.exam.enums.SessionKeyEnum;
import com.bjfu.exam.request.user.LoginRequest;
import com.bjfu.exam.request.user.UserChangePasswordRequest;
import com.bjfu.exam.request.user.UserRegisterRequest;
import com.bjfu.exam.security.annotation.RequireLogin;
import com.bjfu.exam.service.UserService;
import com.bjfu.exam.util.DTOConvertToVOUtil;
import com.bjfu.exam.util.SessionUtil;
import com.bjfu.exam.vo.BaseResult;
import com.bjfu.exam.vo.user.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@Validated
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/loginCheck")
    public BaseResult<UserVO> loginCheck(@Validated @RequestBody LoginRequest loginRequest,
                                               HttpSession session) {
        UserDTO userDTO = userService.loginCheck(loginRequest);
        if(userDTO != null) {
            SessionUtil.initSession(session, userDTO);
            UserVO userDetailVO = DTOConvertToVOUtil.convertUserDTO(userDTO);
            return new BaseResult<>(ResultEnum.SUCCESS, userDetailVO);
        }
        return new BaseResult<>(ResultEnum.LOGIN_FAILED);
    }

    @PutMapping("/register")
    public BaseResult<UserVO> register(@Validated @RequestBody UserRegisterRequest userRegisterRequest,
                                             HttpSession session) {
        UserDTO userDTO = userService.register(userRegisterRequest);
        if(userDTO != null) {
            SessionUtil.initSession(session, userDTO);
            UserVO userDetailVO = DTOConvertToVOUtil.convertUserDTO(userDTO);
            return new BaseResult<>(ResultEnum.SUCCESS, userDetailVO);
        }
        return new BaseResult<>(ResultEnum.ACCOUNT_RECUR);
    }

    @PostMapping("/changePassword")
    public BaseResult<Void> changePassword(@Validated @RequestBody UserChangePasswordRequest userChangePasswordRequest,
                                           HttpSession session) {
        UserDTO userDTO = userService.changePassword(userChangePasswordRequest);
        if(userDTO != null) {
            SessionUtil.deleteSession(session);
            return new BaseResult<>(ResultEnum.SUCCESS);
        }
        return new BaseResult<>(ResultEnum.CHANGE_PASSWORD_FAILED);
    }

    @GetMapping("/logout")
    public BaseResult<Void> logout(HttpSession session) {
        SessionUtil.deleteSession(session);
        return new BaseResult<>(ResultEnum.SUCCESS);
    }

    @GetMapping("/getUserInfo")
    @RequireLogin
    public BaseResult<UserVO> getUserInfo(HttpSession session) {
        UserDTO userInfo = userService.getUserInfo((Long) session.getAttribute(SessionKeyEnum.ACCOUNT_ID.getKey()));
        UserVO userVO = DTOConvertToVOUtil.convertUserDTO(userInfo);
        return new BaseResult<>(ResultEnum.SUCCESS, userVO);
    }

}
