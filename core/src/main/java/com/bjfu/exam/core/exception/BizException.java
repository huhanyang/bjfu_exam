package com.bjfu.exam.core.exception;

import com.bjfu.exam.api.enums.ResultEnum;
import lombok.Getter;

/**
 * 业务相关异常
 * @author warthog
 */
@Getter
public class BizException extends RuntimeException {

    private final int code;
    private final String msg;

    public BizException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
    }
    public BizException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }
    public BizException(ResultEnum resultEnum, String msg) {
        super(msg);
        this.code = resultEnum.getCode();
        this.msg = msg;
    }
}
