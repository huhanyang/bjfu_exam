package com.bjfu.exam.exception;

import com.bjfu.exam.enums.ResultEnum;
import lombok.Getter;

@Getter
public class UnauthorizedOperationException extends BaseException{

    private final Long userId;

    public UnauthorizedOperationException(Long userId, ResultEnum resultEnum) {
        super(resultEnum);
        this.userId = userId;
    }

}
