package com.bjfu.exam.dto.answer;

import lombok.Data;


@Data
public class ProblemAnswerDTO {
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
