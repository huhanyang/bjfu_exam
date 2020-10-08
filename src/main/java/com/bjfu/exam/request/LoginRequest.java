package com.bjfu.exam.request;

import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class LoginRequest extends BaseRequest {

    private String account;
    private String password;

    @Override
    public boolean isComplete() {
        if(StringUtils.isEmpty(account) || StringUtils.isEmpty(password)) {
            return false;
        }
        return true;
    }
}
