package com.bjfu.exam.exception;

import com.bjfu.exam.enums.ResultEnum;
import lombok.Getter;

@Getter
public class UserNotExistExceptionExam extends ExamBaseException {

    private final Long userId;

    public UserNotExistExceptionExam(Long userId, ResultEnum resultEnum) {
        super(resultEnum);
        this.userId = userId;
    }

}
