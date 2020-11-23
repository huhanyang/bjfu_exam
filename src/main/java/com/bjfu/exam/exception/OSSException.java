package com.bjfu.exam.exception;

import com.bjfu.exam.enums.ResultEnum;

public class OSSException extends BaseException {
    public OSSException(ResultEnum resultEnum) {
        super(resultEnum);
    }
}
