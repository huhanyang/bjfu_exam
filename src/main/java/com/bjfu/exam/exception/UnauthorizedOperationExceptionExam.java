package com.bjfu.exam.exception;

import com.bjfu.exam.enums.ResultEnum;
import lombok.Getter;

@Getter
public class UnauthorizedOperationExceptionExam extends ExamBaseException {

    private final Long userId;

    public UnauthorizedOperationExceptionExam(Long userId, ResultEnum resultEnum) {
        super(resultEnum);
        this.userId = userId;
    }

}
