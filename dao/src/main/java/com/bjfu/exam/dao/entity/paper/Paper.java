package com.bjfu.exam.dao.entity.paper;

import com.bjfu.exam.dao.entity.BaseEntity;
import com.bjfu.exam.api.enums.PaperStateEnum;
import com.bjfu.exam.dao.entity.user.User;
import com.bjfu.exam.dao.entity.answer.PaperAnswer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "exam_paper",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"code"})},
        indexes = {@Index(name = "Creator_CreatedTime_Index", columnList = "creator_id"),
                @Index(name = "Creator_CreatedTime_Index", columnList = "createdTime")})
public class Paper extends BaseEntity {
    /**
     * 创建者
     */
    @ManyToOne
    private User creator;
    /**
     * 试卷代号
     */
    @Column(length=64, unique = true)
    private String code;
    /**
     * 试卷状态
     */
    @Column(nullable=false)
    private PaperStateEnum state;
    /**
     * 试卷标题
     */
    @Column(length=64, nullable=false)
    private String title;
    /**
     * 试卷简介
     */
    @Column(length=256, nullable=false)
    private String introduction;
    /**
     * 最长答题时间(分钟)
     */
    @Column(nullable=false)
    private Integer time;
    /**
     * 试卷收集项(JSON数组)
     */
    @Column(length=256, nullable=false)
    private String collection;

    /**
     * 试卷的题目
     */
    @OneToMany(mappedBy = "paper")
    @OrderBy("sort")
    private List<Problem> problems = new ArrayList<>();
    /**
     * 所有答卷
     */
    @OneToMany(mappedBy = "paper")
    @OrderBy("createdTime")
    private List<PaperAnswer> paperAnswers = new ArrayList<>();
}
