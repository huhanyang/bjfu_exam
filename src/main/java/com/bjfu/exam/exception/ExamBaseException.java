package com.bjfu.exam.exception;

import com.bjfu.exam.enums.ResultEnum;
import lombok.Getter;

@Getter
public class ExamBaseException extends RuntimeException {

    private final ResultEnum resultEnum;

    public ExamBaseException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.resultEnum = resultEnum;
    }
}
