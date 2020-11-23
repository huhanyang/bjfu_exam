package com.bjfu.exam.request.paper;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class PaperStateChangeRequest {

    @NotNull(message = "试卷id不能为空!")
    private Long paperId;

    @NotNull(message = "要删除图片的位置不能为空!")
    @Min(value = 1, message = "试卷状态非法!")
    @Max(value = 5, message = "试卷状态非法!")
    private Integer state;

}
