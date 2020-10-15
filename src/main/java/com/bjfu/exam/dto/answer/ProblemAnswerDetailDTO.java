package com.bjfu.exam.dto.answer;

import com.bjfu.exam.dto.paper.ProblemDTO;
import lombok.Data;

@Data
public class ProblemAnswerDetailDTO {
    /**
     * 答题id
     */
    private Long id;
    /**
     * 所属答卷答案
     */
    private PaperAnswerDTO paperAnswer;
    /**
     * 所属题目
     */
    private ProblemDTO problem;
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
