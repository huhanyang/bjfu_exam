package com.bjfu.exam.core.params.answer;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AnswerCreatePaperAnswerParams {
    /**
     * 试卷id
     */
    @NotNull(message = "试卷id不能为空!")
    private Long paperId;
    /**
     * 试卷收集项答案
     */
    @NotBlank(message = "试卷收集项不能为空!")
    private String collectionAnswer;

}
