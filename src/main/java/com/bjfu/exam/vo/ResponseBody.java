package com.bjfu.exam.vo;

import com.bjfu.exam.enums.ResponseBodyEnum;
import lombok.Data;

@Data
public class ResponseBody<T> {
    private int code;
    private String msg;
    private T object;
    public ResponseBody(ResponseBodyEnum responseBodyEnum) {
        this.code = responseBodyEnum.getCode();
        this.msg = responseBodyEnum.getMsg();
    }
    public ResponseBody(ResponseBodyEnum responseBodyEnum, T object) {
        this.code = responseBodyEnum.getCode();
        this.msg = responseBodyEnum.getMsg();
        this.object = object;
    }
}
