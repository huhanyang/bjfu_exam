package com.bjfu.exam.core.exception;

import com.bjfu.exam.api.enums.ResultEnum;
import lombok.Getter;

@Getter
public class UserNotExistExceptionExam extends ExamBaseException {

    private final Long userId;

    public UserNotExistExceptionExam(Long userId, ResultEnum resultEnum) {
        super(resultEnum);
        this.userId = userId;
    }

}
