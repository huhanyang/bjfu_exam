package com.bjfu.exam.dto.export;

import com.bjfu.exam.dto.paper.PaperDTO;
import lombok.Data;

import java.util.Date;

@Data
public class PaperAnswerExportJobDTO {
    /**
     * 导出任务id
     */
    private Long id;
    /**
     * 导出任务所属的试卷
     */
    private PaperDTO paper;
    /**
     * 导出任务执行状态
     */
    private Integer type;
    /**
     * 导出时间
     */
    private Date createTime;
    /**
     * 导出的excel位置
     */
    private String excelUrl;
}
