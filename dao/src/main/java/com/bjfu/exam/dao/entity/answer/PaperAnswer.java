package com.bjfu.exam.dao.entity.answer;

import com.bjfu.exam.dao.entity.BaseEntity;
import com.bjfu.exam.dao.entity.paper.Paper;
import com.bjfu.exam.dao.entity.paper.Problem;
import com.bjfu.exam.dao.entity.user.User;
import com.bjfu.exam.api.enums.PaperAnswerStateEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "exam_paper_answer",
        indexes = {@Index(columnList = "paper_id"),
                @Index(name = "User_CreatedTime_Index", columnList = "user_id"),
                @Index(name = "User_CreatedTime_Index", columnList = "createdTime")})
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
    @Column(nullable=false)
    private PaperAnswerStateEnum state;
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
