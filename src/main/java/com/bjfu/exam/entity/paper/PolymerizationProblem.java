package com.bjfu.exam.entity.paper;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity(name = "exam_polymerization_problem")
public class PolymerizationProblem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    private String title;
    /**
     * 材料
     */
    private String material;
    /**
     * 图片(JSON Array)
     */
    private String images;

    /**
     * 聚合题目的问题
     */
    @OneToMany(mappedBy = "polymerizationProblem")
    private Set<Problem> problems = new HashSet<>();
}
