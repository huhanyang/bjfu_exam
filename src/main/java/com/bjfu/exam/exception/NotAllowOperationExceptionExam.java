package com.bjfu.exam.exception;

import com.bjfu.exam.enums.ResultEnum;

public class NotAllowOperationExceptionExam extends ExamBaseException {

    public NotAllowOperationExceptionExam(ResultEnum resultEnum) {
        super(resultEnum);
    }

}
