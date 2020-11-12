package com.bjfu.exam.vo.paper;

import lombok.Data;

@Data
public class PolymerizationProblemVO {
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
}
