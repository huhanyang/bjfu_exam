package com.bjfu.exam.core.params.user;

import com.bjfu.exam.api.enums.UserTypeEnum;
import com.bjfu.exam.core.params.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserListUsersParams extends PageRequest {
    @NotNull(message = "账号类型不能为空")
    private UserTypeEnum type;
}
