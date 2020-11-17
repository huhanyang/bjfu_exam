package com.bjfu.exam.exception;

import com.bjfu.exam.enums.ResultEnum;
import lombok.Getter;

@Getter
public class UserNotExistException extends BaseException {

    private final Long userId;

    public UserNotExistException(Long userId, ResultEnum resultEnum) {
        super(resultEnum);
        this.userId = userId;
    }

}
