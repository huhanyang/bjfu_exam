package com.bjfu.exam.controller;

import com.bjfu.exam.dto.user.UserDTO;
import com.bjfu.exam.enums.ResultEnum;
import com.bjfu.exam.request.admin.AdminCreateTeacherAccountRequest;
import com.bjfu.exam.security.annotation.RequireAdmin;
import com.bjfu.exam.security.annotation.RequireLogin;
import com.bjfu.exam.service.AdminService;
import com.bjfu.exam.util.DTOConvertToVOUtil;
import com.bjfu.exam.vo.BaseResult;
import com.bjfu.exam.vo.user.UserDetailVO;
import com.bjfu.exam.vo.user.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PutMapping("/createTeacherAccount")
    @RequireLogin
    @RequireAdmin
    public BaseResult<UserVO> createTeacherAccount(@Validated @RequestBody AdminCreateTeacherAccountRequest adminCreateTeacherAccountRequest) {
        UserDTO userDTO = adminService.createTeacherAccount(adminCreateTeacherAccountRequest);
        if(userDTO == null) {
            return new BaseResult<>(ResultEnum.ACCOUNT_RECUR);
        }
        return new BaseResult<>(ResultEnum.SUCCESS, DTOConvertToVOUtil.convertUserDTO(userDTO));
    }

    @GetMapping("/getAllTeacherAccount")
    @RequireLogin
    @RequireAdmin
    public BaseResult<List<UserDetailVO>> getAllTeacherAccount() {
        List<UserDTO> teacherAccountList = adminService.getAllTeacherAccounts();
        List<UserDetailVO> userDetailVOList = teacherAccountList.stream()
                .map(DTOConvertToVOUtil::convertToUserDetailVO)
                .collect(Collectors.toList());
        return new BaseResult<>(ResultEnum.SUCCESS, userDetailVOList);
    }

    @PostMapping("/banTeacherAccount")
    @RequireLogin
    @RequireAdmin
    public BaseResult<Void> banTeacherAccount(@NotNull(message = "教师id不能为空!") Long teacherAccountId) {
        adminService.banTeacherAccount(teacherAccountId);
        return new BaseResult<>(ResultEnum.SUCCESS);
    }

    @PostMapping("/activeTeacherAccount")
    @RequireLogin
    @RequireAdmin
    public BaseResult<Void> activeTeacherAccount(@NotNull(message = "教师id不能为空!") Long teacherAccountId) {
        adminService.activeTeacherAccount(teacherAccountId);
        return new BaseResult<>(ResultEnum.SUCCESS);
    }

}
