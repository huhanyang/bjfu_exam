package com.bjfu.exam.exception;

import com.bjfu.exam.enums.ResultEnum;

public class NotAllowOperationException extends BaseException{

    public NotAllowOperationException(ResultEnum resultEnum) {
        super(resultEnum);
    }

}
