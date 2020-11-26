package com.bjfu.exam.entity.answer;

import com.bjfu.exam.entity.BaseEntity;
import com.bjfu.exam.entity.paper.Problem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "exam_problem_answer",
        indexes = {@Index(name = "PaperAnswer_CreatedTime_Index", columnList = "paper_answer_id"),
        @Index(name = "PaperAnswer_CreatedTime_Index", columnList = "createdTime")})
public class ProblemAnswer extends BaseEntity {
    /**
     * 所属答卷
     */
    @ManyToOne
    private PaperAnswer paperAnswer;
    /**
     * 所属题目
     */
    @ManyToOne
    private Problem problem;
    /**
     * 答题起始时间
     */
    private Date startTime;
    /**
     * 第一次编辑时间
     */
    private Date firstEditTime;
    /**
     * 修改作答耗时(秒)
     */
    private Integer editTime;
    /**
     * 提交时间
     */
    private Date submitTime;
    /**
     * 答案
     */
    @Column(length=512, nullable=false)
    private String answer;
}
