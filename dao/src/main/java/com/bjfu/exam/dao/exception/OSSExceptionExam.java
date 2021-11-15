package com.bjfu.exam.dao.exception;

import com.bjfu.exam.api.enums.ResultEnum;

public class OSSExceptionExam extends RuntimeException {

    private final ResultEnum resultEnum;
    public OSSExceptionExam(ResultEnum resultEnum) {
        this.resultEnum = resultEnum;
    }
}
