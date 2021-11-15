package com.bjfu.exam.request.answer;

import com.bjfu.exam.security.validation.annotation.JSONObject;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PaperAnswerCreateRequest {

    @NotNull(message = "试卷id不能为空!")
    private Long paperId;

    @NotBlank(message = "收集项不能为空!")
    @Length(min = 2, max = 256, message = "收集项长度在2-256位!")
    @JSONObject(message = "收集项答案必须为json对象!")
    private String collectionAnswer;

}
