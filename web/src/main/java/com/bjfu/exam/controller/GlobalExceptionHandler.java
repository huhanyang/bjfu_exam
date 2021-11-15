package com.bjfu.exam.controller;

import com.bjfu.exam.vo.BaseResult;
import com.bjfu.exam.enums.ResultEnum;
import com.bjfu.exam.exception.ExamBaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(ExamBaseException.class)
    public BaseResult<Void> examBaseException(ExamBaseException e){
        log.error("occur exception: {}", e.getResultEnum().getMsg());
        return new BaseResult<>(e.getResultEnum());
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResult<Void> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return new BaseResult<>(ResultEnum.PARAM_WRONG.getCode(),
                ex.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public BaseResult<Void> constraintViolationException(ConstraintViolationException ex) {
        return new BaseResult<>(ResultEnum.PARAM_WRONG.getCode(),
                ex.getConstraintViolations().stream().findFirst().get().getMessage());
    }

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    public BaseResult<Void> runtimeException(RuntimeException e) {
        log.error("occur unKnow exception {}", e.getMessage());
        return new BaseResult<>(ResultEnum.UNKNOWN_EXCEPTION);
    }

}
