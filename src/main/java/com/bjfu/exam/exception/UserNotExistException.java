package com.bjfu.exam.exception;

import com.bjfu.exam.enums.ResponseBodyEnum;
import lombok.Getter;

@Getter
public class UserNotExistException extends BaseException {

    private final Long userId;

    public UserNotExistException(Long userId, ResponseBodyEnum responseBodyEnum) {
        super(responseBodyEnum);
        this.userId = userId;
    }

}
