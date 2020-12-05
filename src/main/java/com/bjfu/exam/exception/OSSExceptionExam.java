package com.bjfu.exam.exception;

import com.bjfu.exam.enums.ResultEnum;

public class OSSExceptionExam extends ExamBaseException {
    public OSSExceptionExam(ResultEnum resultEnum) {
        super(resultEnum);
    }
}
