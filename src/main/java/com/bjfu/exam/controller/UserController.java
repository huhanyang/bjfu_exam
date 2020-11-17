package com.bjfu.exam.controller;

import com.bjfu.exam.dto.user.UserDTO;
import com.bjfu.exam.dto.user.UserDetailDTO;
import com.bjfu.exam.enums.ResultEnum;
import com.bjfu.exam.request.user.LoginRequest;
import com.bjfu.exam.request.user.UserChangePasswordRequest;
import com.bjfu.exam.request.user.UserRegisterRequest;
import com.bjfu.exam.security.annotation.RequireLogin;
import com.bjfu.exam.service.UserService;
import com.bjfu.exam.util.DTOConvertToVOUtil;
import com.bjfu.exam.util.SessionUtil;
import com.bjfu.exam.vo.BaseResult;
import com.bjfu.exam.vo.user.UserDetailVO;
import com.bjfu.exam.vo.user.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/loginCheck")
    public BaseResult<UserDetailVO> loginCheck(@RequestBody LoginRequest loginRequest,
                                               HttpSession session) {
        if(loginRequest.isComplete()) {
            UserDetailDTO userDetailDTO = userService.loginCheck(loginRequest);
            if(userDetailDTO != null) {
                SessionUtil.initSession(session, userDetailDTO);
                UserDetailVO userDetailVO = DTOConvertToVOUtil.convertUserDTOToDetail(userDetailDTO);
                return new BaseResult<>(ResultEnum.SUCCESS, userDetailVO);
            }
            return new BaseResult<>(ResultEnum.LOGIN_FAILED);
        }
        return new BaseResult<>(ResultEnum.PARAM_WRONG);
    }

    @PutMapping("/register")
    public BaseResult<UserDetailVO> register(@RequestBody UserRegisterRequest userRegisterRequest,
                                             HttpSession session) {
        if(userRegisterRequest.isComplete()) {
            UserDetailDTO userDetailDTO = userService.register(userRegisterRequest);
            if(userDetailDTO != null) {
                SessionUtil.initSession(session, userDetailDTO);
                UserDetailVO userDetailVO = DTOConvertToVOUtil.convertUserDTOToDetail(userDetailDTO);
                return new BaseResult<>(ResultEnum.SUCCESS, userDetailVO);
            }
            return new BaseResult<>(ResultEnum.ACCOUNT_RECUR);
        }
        return new BaseResult<>(ResultEnum.PARAM_WRONG);
    }

    @PostMapping("/changePassword")
    public BaseResult<Void> changePassword(@RequestBody UserChangePasswordRequest userChangePasswordRequest,
                                           HttpSession session) {
        if(userChangePasswordRequest.isComplete()) {
            UserDTO userDTO = userService.changePassword(userChangePasswordRequest);
            if(userDTO != null) {
                SessionUtil.deleteSession(session);
                return new BaseResult<>(ResultEnum.SUCCESS);
            }
            return new BaseResult<>(ResultEnum.CHANGE_PASSWORD_FAILED);
        }
        return new BaseResult<>(ResultEnum.PARAM_WRONG);
    }

    @GetMapping("/logout")
    public BaseResult<Void> logout(HttpSession session) {
        SessionUtil.deleteSession(session);
        return new BaseResult<>(ResultEnum.SUCCESS);
    }

    @GetMapping("/getUserDetail")
    @RequireLogin
    public BaseResult<UserDetailVO> getUserDetail(HttpSession session) {
        UserDetailDTO userDetailDTO = userService.getUserDetail((Long) session.getAttribute("userId"));
        UserDetailVO userDetailVO = DTOConvertToVOUtil.convertUserDTOToDetail(userDetailDTO);
        return new BaseResult<>(ResultEnum.SUCCESS, userDetailVO);
    }

    @GetMapping("/getUserInfo")
    @RequireLogin
    public BaseResult<UserVO> getUserInfo(Long id, HttpSession session) {
        if(id == null) {
            return new BaseResult<>(ResultEnum.PARAM_WRONG);
        }
        UserDTO userInfo = userService.getUserInfo(id);
        UserVO userVO = DTOConvertToVOUtil.convertUserDTO(userInfo);
        return new BaseResult<>(ResultEnum.SUCCESS, userVO);
    }

}
