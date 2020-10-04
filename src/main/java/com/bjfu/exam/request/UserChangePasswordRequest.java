package com.bjfu.exam.request;

import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class UserChangePasswordRequest extends BaseRequest {
    /**
     * 账号
     */
    private String account;
    /**
     * 原密码
     */
    private String oldPassword;
    /**
     * 新密码
     */
    private String newPassword;

    @Override
    public boolean isComplete() {
        if(StringUtils.isEmpty(account) || StringUtils.isEmpty(oldPassword)
                || StringUtils.isEmpty(newPassword)) {
            return false;
        }
        return true;
    }
}
