package com.bjfu.exam.exception;

import com.bjfu.exam.enums.ResultEnum;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final ResultEnum resultEnum;

    public BaseException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.resultEnum = resultEnum;
    }
}
