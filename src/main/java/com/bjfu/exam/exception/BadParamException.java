package com.bjfu.exam.exception;

import com.bjfu.exam.enums.ResponseBodyEnum;

public class BadParamException extends BaseException {
    public BadParamException(ResponseBodyEnum responseBodyEnum) {
        super(responseBodyEnum);
    }
}
