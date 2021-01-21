package com.bjfu.exam.entity.paper;

import com.bjfu.exam.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "exam_problem",
        indexes = {@Index(name = "Paper_Sort_Index", columnList = "paper_id"),
                @Index(name ="Paper_Sort_Index", columnList = "sort"),
                @Index(columnList = "father_problem_id")})
public class Problem extends BaseEntity {
    /**
     * 所属试卷
     */
    @ManyToOne
    private Paper paper;
    /**
     * 排序字段
     */
    private Integer sort;
    /**
     * 标题
     */
    @Column(length=64, nullable=false)
    private String title;
    /**
     * 材料
     */
    @Column(length=256, nullable=false)
    private String material;
    /**
     * 图片url(JSON数组)
     */
    @Column(length=256, nullable=false)
    private String images;
    /**
     * 类型
     */
    @Column(nullable=false)
    private Integer type;
    /**
     * 选择题可选答案(JSON数组)
     */
    @Column(length=512)
    private String answer;

    /**
     * 所属复合题
     */
    @ManyToOne
    private Problem fatherProblem;
    /**
     * 复合题的小题
     */
    @OneToMany(mappedBy = "fatherProblem")
    @OrderBy("sort")
    private List<Problem> subProblems = new ArrayList<>();

}
