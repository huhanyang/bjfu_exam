package com.bjfu.exam.controller;

import com.bjfu.exam.dto.user.UserDTO;
import com.bjfu.exam.dto.user.UserDetailDTO;
import com.bjfu.exam.enums.ResponseBodyEnum;
import com.bjfu.exam.request.LoginRequest;
import com.bjfu.exam.request.UserChangePasswordRequest;
import com.bjfu.exam.request.UserRegisterRequest;
import com.bjfu.exam.service.UserService;
import com.bjfu.exam.util.DTOConvertToVOUtil;
import com.bjfu.exam.util.SessionUtil;
import com.bjfu.exam.vo.ResponseBody;
import com.bjfu.exam.vo.user.UserDetailVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/loginCheck")
    public ResponseBody<UserDetailVO> loginCheck(@RequestBody LoginRequest loginRequest,
                                         HttpSession session) {
        if(loginRequest.isComplete()) {
            UserDetailDTO userDetailDTO = userService.loginCheck(loginRequest);
            if(userDetailDTO != null) {
                SessionUtil.initSession(session, userDetailDTO);
                UserDetailVO userDetailVO = DTOConvertToVOUtil.convertUserDTOToDetail(userDetailDTO);
                return new ResponseBody<>(ResponseBodyEnum.SUCCESS, userDetailVO);
            }
            return new ResponseBody<>(ResponseBodyEnum.LOGIN_FAILED);
        }
        return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
    }

    @PutMapping("/register")
    public ResponseBody<UserDetailVO> register(@RequestBody UserRegisterRequest userRegisterRequest,
                                       HttpSession session) {
        if(userRegisterRequest.isComplete()) {
            UserDetailDTO userDetailDTO = userService.register(userRegisterRequest);
            if(userDetailDTO != null) {
                SessionUtil.initSession(session, userDetailDTO);
                UserDetailVO userDetailVO = DTOConvertToVOUtil.convertUserDTOToDetail(userDetailDTO);
                return new ResponseBody<>(ResponseBodyEnum.SUCCESS, userDetailVO);
            }
            return new ResponseBody<>(ResponseBodyEnum.ACCOUNT_RECUR);
        }
        return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
    }

    @PostMapping("/changePassword")
    public ResponseBody<Void> changePassword(@RequestBody UserChangePasswordRequest userChangePasswordRequest,
                                             HttpSession session) {
        if(userChangePasswordRequest.isComplete()) {
            UserDTO userDTO = userService.changePassword(userChangePasswordRequest);
            if(userDTO != null) {
                SessionUtil.deleteSession(session);
                return new ResponseBody<>(ResponseBodyEnum.SUCCESS);
            }
            return new ResponseBody<>(ResponseBodyEnum.CHANGE_PASSWORD_FAILED);
        }
        return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
    }

    @GetMapping("/logout")
    public ResponseBody<Void> logout(HttpSession session) {
        SessionUtil.deleteSession(session);
        return new ResponseBody<>(ResponseBodyEnum.SUCCESS);
    }

    @GetMapping("/getUserDetail")
    public ResponseBody<UserDetailVO> getUserDetail(Long id, HttpSession session) {
        if(id == null) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
        } else {
            if(SessionUtil.existSession(session)) {
                UserDetailDTO userDetailDTO = userService.getUserDetail(id);
                UserDetailVO userDetailVO = DTOConvertToVOUtil.convertUserDTOToDetail(userDetailDTO);
                return new ResponseBody<>(ResponseBodyEnum.SUCCESS, userDetailVO);
            } else {
                return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
            }
        }
    }

}
