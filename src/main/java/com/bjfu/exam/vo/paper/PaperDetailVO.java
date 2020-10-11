package com.bjfu.exam.vo.paper;

import com.bjfu.exam.vo.user.UserVO;
import lombok.Data;

import java.util.List;


@Data
public class PaperDetailVO {
    /**
     * 试卷id
     */
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
     * 导出excel位置
     */
    private String excelUrl;
    /**
     * 创建者
     */
    private UserVO creator;
    /**
     * 试卷状态
     */
    private int status;
    /**
     * 试卷的题目
     */
    private List<ProblemVO> problems;
    /**
     * 试卷的聚合题目
     */
    private List<PolymerizationProblemVO> polymerizationProblems;
}
