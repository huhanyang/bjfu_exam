package com.bjfu.exam.core.exception;

import com.bjfu.exam.api.enums.ResultEnum;

public class NotAllowOperationExceptionExam extends ExamBaseException {

    public NotAllowOperationExceptionExam(ResultEnum resultEnum) {
        super(resultEnum);
    }

}
