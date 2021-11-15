package com.bjfu.exam.security.validation.annotation;

import com.bjfu.exam.security.validation.validator.JSONArrayValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = JSONArrayValidator.class)
public @interface JSONArray {

    String message() default "非json数组格式";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
