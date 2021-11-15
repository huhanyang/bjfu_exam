package com.bjfu.exam.core.params.user;

import com.bjfu.exam.api.enums.UserStateEnum;
import com.bjfu.exam.api.enums.UserTypeEnum;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserRegisterParams {

    @NotBlank(message = "账号不能为空!")
    @Length(min = 8, max = 32, message = "账号长度在8-32位!")
    private String account;

    @NotBlank(message = "密码不能为空!")
    @Length(min = 8, max = 32, message = "密码长度在8-32位!")
    private String password;

    @NotBlank(message = "真实姓名不能为空!")
    @Length(min = 1, max = 32, message = "真实姓名长度1到32个字符!")
    private String name;

    @NotNull(message = "注册的用户类型不能为空!")
    private UserTypeEnum type;

    @NotNull(message = "注册的用户初始状态不能为空!")
    private UserStateEnum state;
}
