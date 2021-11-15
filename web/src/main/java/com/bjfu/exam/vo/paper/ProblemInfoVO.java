package com.bjfu.exam.vo.paper;

import lombok.Data;

@Data
public class ProblemInfoVO {
    /**
     * 题目id
     */
    private Long id;
    /**
     * 所属组合题目
     */
    private ProblemInfoVO fatherProblem;
    /**
     * 排序字段
     */
    private Integer sort;
    /**
     * 标题
     */
    private String title;
    /**
     * 材料
     */
    private String material;
    /**
     * 图片(JSON Array)
     */
    private String images;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 答案(JSON)
     */
    private String answer;
}
