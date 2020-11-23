package com.bjfu.exam.controller;

import com.bjfu.exam.dto.user.UserDTO;
import com.bjfu.exam.enums.ResultEnum;
import com.bjfu.exam.enums.SessionKeyEnum;
import com.bjfu.exam.request.admin.AdminCreateTeacherAccountRequest;
import com.bjfu.exam.security.annotation.RequireAdmin;
import com.bjfu.exam.security.annotation.RequireLogin;
import com.bjfu.exam.service.AdminService;
import com.bjfu.exam.util.DTOConvertToVOUtil;
import com.bjfu.exam.vo.BaseResult;
import com.bjfu.exam.vo.user.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@Validated
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PutMapping("/createTeacherAccount")
    @RequireLogin
    @RequireAdmin
    public BaseResult<UserVO> createTeacherAccount(@Validated @RequestBody AdminCreateTeacherAccountRequest adminCreateTeacherAccountRequest,
                                                   HttpSession session) {
        Long userId = (Long) session.getAttribute(SessionKeyEnum.ACCOUNT_ID.getKey());
        UserDTO userDTO = adminService.createTeacherAccount(userId, adminCreateTeacherAccountRequest);
        if(userDTO == null) {
            return new BaseResult<>(ResultEnum.ACCOUNT_RECUR);
        }
        return new BaseResult<>(ResultEnum.SUCCESS, DTOConvertToVOUtil.convertUserDTO(userDTO));
    }

}
