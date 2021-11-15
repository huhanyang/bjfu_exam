package com.bjfu.exam.dao.exception;

import com.bjfu.exam.api.enums.ResultEnum;
import lombok.Getter;

@Getter
public class DaoException {

    private final ResultEnum resultEnum;

    public DaoException(ResultEnum resultEnum) {
        this.resultEnum = resultEnum;
    }
}
