package com.bjfu.exam.dto;

import lombok.Data;

@Data
public class PaperDTO {
    /**
     * 试卷代号
     */
    private String code;
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
     * 试卷收集项json格式
     */
    private String collection;
    /**
     * 创建者姓名
     */
    private String creatorName;
    /**
     * 导出excel位置
     */
    private String excelUrl;
    /**
     * 试卷状态
     */
    private int status;
    /**
     * 题目总数
     */
    private int problemCount;
}
