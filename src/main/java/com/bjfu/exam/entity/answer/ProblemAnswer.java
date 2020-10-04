package com.bjfu.exam.entity.answer;

import com.bjfu.exam.entity.paper.Paper;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "exam_problem_answer")
public class ProblemAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 所属答卷答案
     */
    @ManyToOne
    private PaperAnswer paperAnswer;
    /**
     * 所属题目
     */
    @ManyToOne
    private Paper paper;
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
