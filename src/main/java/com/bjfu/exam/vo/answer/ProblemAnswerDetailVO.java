package com.bjfu.exam.vo.answer;

import com.bjfu.exam.vo.paper.ProblemVO;
import lombok.Data;

@Data
public class ProblemAnswerDetailVO {
    /**
     * 答题id
     */
    private Long id;
    /**
     * 所属答卷答案
     */
    private PaperAnswerVO paperAnswer;
    /**
     * 所属题目
     */
    private ProblemVO problem;
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
