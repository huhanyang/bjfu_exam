package com.bjfu.exam.core.exception;

import com.bjfu.exam.api.enums.ResultEnum;

public class OSSExceptionExam extends ExamBaseException {
    public OSSExceptionExam(ResultEnum resultEnum) {
        super(resultEnum);
    }
}
