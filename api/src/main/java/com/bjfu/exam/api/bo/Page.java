package com.bjfu.exam.api.bo;

import lombok.Data;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class Page<T> {
    private Long totalElements;
    private Integer totalPages;
    private Integer size;
    private Long pageLength;
    private List<T> content;

    public <U> Page<U> map(Function<T, U> converter) {
        Page<U> newPage = new Page<>();
        newPage.totalElements = totalElements;
        newPage.totalPages = totalPages;
        newPage.size = size;
        newPage.pageLength = pageLength;
        newPage.content = content.stream().map(converter).collect(Collectors.toList());
        return newPage;
    }
}
