package com.bjfu.exam.core.params.paper;

import lombok.Data;

@Data
public class PaperEditPaperParams {
    /**
     * 要编辑的试卷的id
     */
    private Long paperId;
    /**
     * 试卷标题
     */
    private String title;
    /**
     * 试卷简介
     */
    private String introduction;
    /**
     * 最长答题时间(分钟)
     */
    private Integer time;
    /**
     * 试卷收集项(JSON数组)
     */
    private String collection;
}
