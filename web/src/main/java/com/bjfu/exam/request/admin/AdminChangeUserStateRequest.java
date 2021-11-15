package com.bjfu.exam.request.admin;

import com.bjfu.exam.api.enums.UserStateEnum;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AdminChangeUserStateRequest {

    @NotBlank(message = "账号不能为空!")
    @Length(min = 8, max = 32, message = "账号长度在8-32位!")
    private String userAccount;

    @NotNull(message = "新状态不能为空!")
    private UserStateEnum newState;
}
