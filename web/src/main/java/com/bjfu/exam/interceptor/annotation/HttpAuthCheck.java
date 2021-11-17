package com.bjfu.exam.interceptor.annotation;

import com.bjfu.exam.api.enums.UserStateEnum;
import com.bjfu.exam.api.enums.UserTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注释在controller接口上 做前置校验
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface HttpAuthCheck {
    /**
     * 允许访问的用户类型
     */
    UserTypeEnum[] allowUserTypes() default {UserTypeEnum.ADMIN, UserTypeEnum.TEACHER, UserTypeEnum.STUDENT};
    /**
     * 允许访问的用户状态
     */
    UserStateEnum[] allowUserStates() default {UserStateEnum.ACTIVE};
    /**
     * 是否需要登录
     */
    boolean needLogin() default true;
}
