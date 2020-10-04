package com.bjfu.exam.entity.paper;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "exam_problem")
public class Problem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 所属试卷
     */
    @ManyToOne
    private Paper paper;
    /**
     * 聚合题干
     */
    @ManyToOne
    private PolymerizationProblem polymerizationProblem;
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
     * 类型
     */
    private Integer type;
    /**
     * 答案(JSON)
     */
    private String answer;
}
