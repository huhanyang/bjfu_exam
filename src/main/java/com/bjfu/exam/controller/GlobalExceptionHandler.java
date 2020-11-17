package com.bjfu.exam.controller;

import com.bjfu.exam.exception.*;
import com.bjfu.exam.vo.BaseResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(BadParamException.class)
    public BaseResult<Void> badParamException(BadParamException ex){
        return new BaseResult<>(ex.getResultEnum());
    }

    @ResponseBody
    @ExceptionHandler(DataDamageException.class)
    public BaseResult<Void> dataDamageException(DataDamageException ex){
        return new BaseResult<>(ex.getResultEnum());
    }

    @ResponseBody
    @ExceptionHandler(NotAllowOperationException.class)
    public BaseResult<Void> notAllowOperationException(NotAllowOperationException ex){
        return new BaseResult<>(ex.getResultEnum());
    }

    @ResponseBody
    @ExceptionHandler(UnauthorizedOperationException.class)
    public BaseResult<Void> unauthorizedOperationException(UnauthorizedOperationException ex){
        return new BaseResult<>(ex.getResultEnum());
    }

    @ResponseBody
    @ExceptionHandler(UserNotExistException.class)
    public BaseResult<Void> userNotExistException(UserNotExistException ex){
        return new BaseResult<>(ex.getResultEnum());
    }

}
