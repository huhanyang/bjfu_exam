package com.bjfu.exam.request.admin;

import com.bjfu.exam.api.enums.UserTypeEnum;
import com.bjfu.exam.request.BasePageListRequest;

import javax.validation.constraints.NotNull;

public class AdminListUsersRequest extends BasePageListRequest {
    @NotNull(message = "查询的用户类型不能为空!")
    private UserTypeEnum type;
}
