package com.bjfu.exam.request.paper;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 编辑试卷的标题与介绍请求
 * @author warthog
 */
@Data
public class PaperEditPaperRequest {

    @NotNull(message = "试卷id不能为空!")
    private Long paperId;

    @NotBlank(message = "试卷标题不能为空!")
    @Length(min = 1, max = 64, message = "试卷标题长度在1-64位!")
    private String title;

    @NotBlank(message = "试卷简介不能为空!")
    @Length(min = 1, max = 256, message = "试卷简介长度在1-256位!")
    private String introduction;
}
