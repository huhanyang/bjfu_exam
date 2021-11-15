package com.bjfu.exam.core.params;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class PageRequest {
    @NotNull(message = "当前页号不能为空")
    @Min(value = 0, message = "页号不能小于0")
    private Integer page;

    @NotNull(message = "分页的内容数不能为空")
    @Min(value = 1, message = "分页的内容数不能小于1")
    private Integer size;
}
