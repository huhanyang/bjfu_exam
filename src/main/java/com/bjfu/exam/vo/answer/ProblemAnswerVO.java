package com.bjfu.exam.vo.answer;

import lombok.Data;

@Data
public class ProblemAnswerVO {
    /**
     * 答题id
     */
    private Long id;
    /**
     * 题目总耗时
     */
    private Integer totalTime;
    /**
     * 修改作答耗时
     */
    private Integer editTime;
    /**
     * 答案
     */
    private String answer;
}
