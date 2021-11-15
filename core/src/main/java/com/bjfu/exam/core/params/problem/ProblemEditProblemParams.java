package com.bjfu.exam.core.params.problem;

import lombok.Data;

@Data
public class ProblemEditProblemParams {
    /**
     * 所属试卷id
     */
    private Long paperId;
    /**
     * 题目id
     */
    private Long problemId;
    /**
     * 标题
     */
    private String title;
    /**
     * 材料
     */
    private String material;
    /**
     * 选择题可选答案(JSON数组)
     */
    private String answer;
}
