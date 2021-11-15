package com.bjfu.exam.exception;

import com.bjfu.exam.enums.ResultEnum;

public class BadParamExceptionExam extends ExamBaseException {
    public BadParamExceptionExam(ResultEnum resultEnum) {
        super(resultEnum);
    }
}
