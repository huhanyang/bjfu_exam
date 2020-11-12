package com.bjfu.exam.vo.paper;

import lombok.Data;

import java.util.List;

@Data
public class PolymerizationProblemDetailVO {
    /**
     * 聚合题目id
     */
    private Long id;
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
     * 组合题目所属的子问题
     */
    List<ProblemVO> problems;
}
