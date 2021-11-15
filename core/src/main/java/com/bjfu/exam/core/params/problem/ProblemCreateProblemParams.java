package com.bjfu.exam.core.params.problem;

import com.bjfu.exam.api.enums.ProblemTypeEnum;
import lombok.Data;

@Data
public class ProblemCreateProblemParams {
    /**
     * 所属试卷id
     */
    private Long paperId;
    /**
     * 所属复合题id
     */
    private Long fatherProblemId;
    /**
     * 题号
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
     * 类型
     */
    private ProblemTypeEnum type;
    /**
     * 选择题可选答案(JSON数组)
     */
    private String answer;
}
