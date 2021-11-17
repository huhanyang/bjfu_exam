package com.bjfu.exam.controller;

import com.bjfu.exam.api.bo.Page;
import com.bjfu.exam.api.enums.UserStateEnum;
import com.bjfu.exam.api.enums.UserTypeEnum;
import com.bjfu.exam.core.ao.UserAO;
import com.bjfu.exam.core.dto.user.UserDTO;
import com.bjfu.exam.core.params.user.UserChangeUserStateParams;
import com.bjfu.exam.core.params.user.UserListUsersParams;
import com.bjfu.exam.core.params.user.UserRegisterParams;
import com.bjfu.exam.request.admin.AdminChangeUserStateRequest;
import com.bjfu.exam.request.admin.AdminListUsersRequest;
import com.bjfu.exam.vo.BaseResult;
import com.bjfu.exam.vo.user.UserDetailVO;
import com.bjfu.exam.vo.user.UserVO;
import com.bjfu.exam.request.user.UserRegisterRequest;
import com.bjfu.exam.utils.DTOConvertToVOUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@Validated
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserAO userAO;

    @PutMapping("/createTeacher")
    public BaseResult<UserVO> createTeacher(@Validated @RequestBody UserRegisterRequest request) {
        UserRegisterParams userRegisterParams = new UserRegisterParams();
        BeanUtils.copyProperties(request, userRegisterParams);
        userRegisterParams.setType(UserTypeEnum.TEACHER);
        userRegisterParams.setState(UserStateEnum.ACTIVE);

        UserDTO teacherDTO = userAO.register(userRegisterParams);
        return BaseResult.success(DTOConvertToVOUtil.convertUserDTO(teacherDTO));
    }

    @GetMapping("/listUsers")
    public BaseResult<Page<UserDetailVO>> listUsers(@Validated AdminListUsersRequest request) {
        UserListUsersParams userListUsersParams = new UserListUsersParams();
        BeanUtils.copyProperties(request, userListUsersParams);

        Page<UserDTO> userDTOPage = userAO.listUsersByType(userListUsersParams);
        return BaseResult.success(userDTOPage.map(DTOConvertToVOUtil::convertToUserDetailVO));
    }

    @PostMapping("/changeUserState")
    public BaseResult<UserVO> banTeacherAccount(@Validated @RequestBody AdminChangeUserStateRequest request) {
        UserChangeUserStateParams userChangeUserStateParams = new UserChangeUserStateParams();
        BeanUtils.copyProperties(request, userChangeUserStateParams);

        UserDTO userDTO = userAO.changeUserState(userChangeUserStateParams);
        return BaseResult.success(DTOConvertToVOUtil.convertUserDTO(userDTO));
    }

}
