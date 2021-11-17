package com.bjfu.exam.interceptor.validation.validator;

import com.alibaba.fastjson.JSONObject;
import com.bjfu.exam.interceptor.validation.annotation.JSONArray;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class JSONObjectValidator implements ConstraintValidator<JSONArray, String> {

    @Override
    public void initialize(JSONArray constraintAnnotation) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(s.isBlank()) {
            return false;
        }
        try {
            Object parse = JSONObject.parse(s);
            if(parse instanceof com.alibaba.fastjson.JSONObject) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

}
