package com.bjfu.exam.exception;

import com.bjfu.exam.enums.ResponseBodyEnum;

public class NotAllowOperationException extends BaseException{

    public NotAllowOperationException(ResponseBodyEnum responseBodyEnum) {
        super(responseBodyEnum);
    }

}
