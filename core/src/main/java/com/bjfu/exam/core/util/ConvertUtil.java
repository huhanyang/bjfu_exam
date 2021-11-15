package com.bjfu.exam.core.util;

import com.bjfu.exam.api.bo.Page;

import java.util.function.Function;

public class ConvertUtil {

    public static <T> Page<T> convertJpaPageToExamPage(org.springframework.data.domain.Page<T> jpaPage) {
        Page<T> examPage = new Page<>();
        examPage.setTotalElements(jpaPage.getTotalElements());
        examPage.setTotalPages(jpaPage.getTotalPages());
        examPage.setSize(jpaPage.getSize());
        examPage.setContent(jpaPage.getContent());
        return examPage;
    }

    public static <T, U> Page<U> convertJpaPageToExamPage(org.springframework.data.domain.Page<T> jpaPage, Function<T, U> converter) {
        org.springframework.data.domain.Page<U> newJpaPage = jpaPage.map(converter);
        Page<U> examPage = new Page<>();
        examPage.setTotalElements(jpaPage.getTotalElements());
        examPage.setTotalPages(jpaPage.getTotalPages());
        examPage.setSize(jpaPage.getSize());
        examPage.setContent(newJpaPage.getContent());
        return examPage;
    }
}
