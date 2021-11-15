package com.bjfu.exam.core.dto;

import lombok.Data;

@Data
public class BaseResult<T> {
    private Integer errorCode;
    private String errorMsg;
    private T object;
}
