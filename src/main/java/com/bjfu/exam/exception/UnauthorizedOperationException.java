package com.bjfu.exam.exception;

import com.bjfu.exam.enums.ResponseBodyEnum;
import lombok.Getter;

@Getter
public class UnauthorizedOperationException extends BaseException{

    private final Long userId;

    public UnauthorizedOperationException(Long userId, ResponseBodyEnum responseBodyEnum) {
        super(responseBodyEnum);
        this.userId = userId;
    }

}
