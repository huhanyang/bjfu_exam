package com.bjfu.exam.exception;

import com.bjfu.exam.enums.ResponseBodyEnum;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final ResponseBodyEnum responseBodyEnum;

    public BaseException(ResponseBodyEnum responseBodyEnum) {
        super(responseBodyEnum.getMsg());
        this.responseBodyEnum = responseBodyEnum;
    }
}
