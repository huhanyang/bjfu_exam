package com.bjfu.exam.vo.paper;

import com.bjfu.exam.dto.export.PaperAnswerExportJobDTO;
import com.bjfu.exam.vo.export.PaperAnswerExportJobVO;
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
     * 创建者
     */
    private UserVO creator;
    /**
     * 导出过的excel任务
     */
    private List<PaperAnswerExportJobVO> paperAnswerExportJobs;
    /**
     * 试卷状态
     */
    private Integer state;
    /**
     * 试卷的题目
     */
    private List<ProblemVO> problems;
    /**
     * 试卷的聚合题目
     */
    private List<PolymerizationProblemVO> polymerizationProblems;
}
