package com.bjfu.exam.request.paper;

import com.bjfu.exam.interceptor.validation.annotation.JSONArray;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PaperCreatePaperRequest {

    @NotBlank(message = "试卷标题不能为空!")
    @Length(min = 1, max = 64, message = "试卷标题长度在1-64位!")
    private String title;

    @NotBlank(message = "试卷简介不能为空!")
    @Length(min = 1, max = 256, message = "试卷简介长度在1-256位!")
    private String introduction;

    @NotNull(message = "试卷最长作答时间不能为空!")
    @Min(value = 1,message = "试卷作答时间禁止小于1分钟!")
    @Max(value = 3600, message = "试卷作答时间禁止大于3600分钟!")
    private Integer time;

    @NotBlank(message = "试卷收集项不能为空!")
    @Length(min = 2, max = 64, message = "试卷收集项长度在2-64位!")
    @JSONArray(message = "试卷收集项应该为json数组!")
    private String collection;

}
