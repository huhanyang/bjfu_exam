package com.bjfu.exam.exception;

import com.bjfu.exam.enums.ResultEnum;
import lombok.Getter;

@Getter
public class DataDamageException extends BaseException {
    private String info;
    public DataDamageException(String info, ResultEnum resultEnum) {
        super(resultEnum);
    }
}
