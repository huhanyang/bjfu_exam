package com.bjfu.exam.entity.answer;

import com.bjfu.exam.entity.BaseEntity;
import com.bjfu.exam.entity.paper.Problem;
import com.bjfu.exam.entity.user.User;
import com.bjfu.exam.entity.paper.Paper;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity(name = "exam_paper_answer")
public class PaperAnswer extends BaseEntity {
    /**
     * 所答试卷
     */
    @ManyToOne
    private Paper paper;
    /**
     * 答题用户
     */
    @ManyToOne
    private User user;
    /**
     * 收集项答案(JSON)
     */
    @Column(length=256, nullable=false)
    private String collectionAnswer;
    /**
     * 答卷状态
     */
    private Integer state;
    /**
     * 答题结束时间
     */
    private Date finishTime;
    /**
     * 下一道题
     */
    @ManyToOne
    private Problem nextProblem;

    @OneToMany(mappedBy = "paperAnswer")
    @OrderBy("createdTime")
    List<ProblemAnswer> problemAnswers = new ArrayList<>();
}
