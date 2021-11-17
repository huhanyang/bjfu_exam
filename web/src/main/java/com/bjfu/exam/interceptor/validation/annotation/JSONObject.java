package com.bjfu.exam.interceptor.validation.annotation;

import com.bjfu.exam.interceptor.validation.validator.JSONObjectValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = JSONObjectValidator.class)
public @interface JSONObject {

    String message() default "非json对象格式";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
