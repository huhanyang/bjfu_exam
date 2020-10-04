package com.bjfu.exam.request;

import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class UserRegisterRequest extends BaseRequest {
    /**
     * 账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * 姓名
     */
    private String name;
    /**
     * 类型
     */
    private Integer type;

    @Override
    public boolean isComplete() {
        if(StringUtils.isEmpty(account) || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(name) || type == null) {
            return false;
        }
        return true;
    }

}
