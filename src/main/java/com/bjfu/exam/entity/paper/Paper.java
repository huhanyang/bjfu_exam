package com.bjfu.exam.entity.paper;

import com.bjfu.exam.entity.user.User;
import com.bjfu.exam.entity.answer.PaperAnswer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity(name = "exam_paper")
public class Paper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 试卷代号
     */
    private String code;
    /**
     * 试卷标题
     */
    private String title;
    /**
     * 试卷简介
     */
    private String introduction;
    /**
     * 最长答题时间(分钟)
     */
    private Integer time;
    /**
     * 试卷收集项json格式
     */
    private String collection;
    /**
     * 创建者
     */
    @ManyToOne
    private User creator;
    /**
     * 导出excel位置
     */
    private String excelUrl;
    /**
     * 试卷状态
     */
    private Integer state;

    /**
     * 试卷的题目
     */
    @OneToMany(mappedBy = "paper")
    private Set<Problem> problems = new HashSet<>();
    /**
     * 试卷的聚合题目
     */
    @OneToMany(mappedBy = "paper")
    private Set<PolymerizationProblem> polymerizationProblems = new HashSet<>();
    /**
     * 所有作答
     */
    @OneToMany(mappedBy = "paper")
    private Set<PaperAnswer> paperAnswers = new HashSet<>();
}
