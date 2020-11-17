package com.bjfu.exam.exception;

import com.bjfu.exam.enums.ResultEnum;

public class FileUploadException extends BaseException {

    public FileUploadException(ResultEnum resultEnum) {
        super(resultEnum);
    }
}
