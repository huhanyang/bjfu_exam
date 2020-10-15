package com.bjfu.exam.exception;

import com.bjfu.exam.enums.ResponseBodyEnum;
import lombok.Getter;

@Getter
public class DataDamageException extends BaseException {
    private String info;
    public DataDamageException(String info, ResponseBodyEnum responseBodyEnum) {
        super(responseBodyEnum);
    }
}
