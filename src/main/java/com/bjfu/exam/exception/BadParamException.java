package com.bjfu.exam.exception;

import com.bjfu.exam.enums.ResultEnum;

public class BadParamException extends BaseException {
    public BadParamException(ResultEnum resultEnum) {
        super(resultEnum);
    }
}
