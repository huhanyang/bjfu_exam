package com.bjfu.exam.request.paper;

import com.bjfu.exam.api.enums.PaperStateEnum;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class PaperChangePaperStateRequest {

    @NotNull(message = "试卷id不能为空!")
    private Long paperId;

    @NotNull(message = "要删除图片的位置不能为空!")
    private PaperStateEnum newState;

}
