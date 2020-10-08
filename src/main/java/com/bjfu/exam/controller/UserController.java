package com.bjfu.exam.controller;

import com.bjfu.exam.dto.UserDTO;
import com.bjfu.exam.enums.ResponseBodyEnum;
import com.bjfu.exam.request.LoginRequest;
import com.bjfu.exam.request.UserChangePasswordRequest;
import com.bjfu.exam.request.UserRegisterRequest;
import com.bjfu.exam.service.UserService;
import com.bjfu.exam.vo.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/loginCheck")
    public ResponseBody<Void> loginCheck(@RequestBody LoginRequest loginRequest,
                                         HttpSession session) {
        if(loginRequest.isComplete()) {
            UserDTO userDTO = userService.loginCheck(loginRequest);
            if(userDTO != null) {
                session.setAttribute("userId", userDTO.getId());
                session.setAttribute("account", userDTO.getAccount());
                session.setAttribute("type", userDTO.getType());
                return new ResponseBody<>(ResponseBodyEnum.SUCCESS);
            }
            return new ResponseBody<>(ResponseBodyEnum.LOGIN_FAILED);
        }
        return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
    }

    @PutMapping("/register")
    public ResponseBody<Void> register(@RequestBody UserRegisterRequest userRegisterRequest,
                                       HttpSession session) {
        if(userRegisterRequest.isComplete()) {
            UserDTO userDTO = userService.register(userRegisterRequest);
            if(userDTO != null) {
                session.setAttribute("userId", userDTO.getId());
                session.setAttribute("account", userDTO.getAccount());
                session.setAttribute("type", userDTO.getType());
                return new ResponseBody<>(ResponseBodyEnum.SUCCESS);
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
                session.invalidate();
                return new ResponseBody<>(ResponseBodyEnum.SUCCESS);
            }
            return new ResponseBody<>(ResponseBodyEnum.CHANGE_PASSWORD_FAILED);
        }
        return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
    }

    @GetMapping("/logout")
    public ResponseBody<Void> logout(HttpSession session) {
        session.invalidate();
        return new ResponseBody<>(ResponseBodyEnum.SUCCESS);
    }

}
