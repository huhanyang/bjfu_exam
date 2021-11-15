package com.bjfu.exam.core.exception;

import com.bjfu.exam.api.enums.ResultEnum;

public class BadParamExceptionExam extends ExamBaseException {
    public BadParamExceptionExam(ResultEnum resultEnum) {
        super(resultEnum);
    }
}
