package com.bjfu.exam.controller;

import com.bjfu.exam.enums.ResultEnum;
import com.bjfu.exam.exception.*;
import com.bjfu.exam.vo.BaseResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(BadParamException.class)
    public BaseResult<Void> badParamException(BadParamException ex){
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

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResult<Void> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return new BaseResult<>(ResultEnum.PARAM_WRONG.getCode(), ex.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public BaseResult<Void> constraintViolationException(ConstraintViolationException ex) {
        return new BaseResult<>(ResultEnum.PARAM_WRONG.getCode(),
                ex.getConstraintViolations().stream().findFirst().get().getMessage());
    }

    @ResponseBody
    @ExceptionHandler(OSSException.class)
    public BaseResult<Void> oSSException(OSSException ex) {
        return new BaseResult<>(ex.getResultEnum());
    }



}
