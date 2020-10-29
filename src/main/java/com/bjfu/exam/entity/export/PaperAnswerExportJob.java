package com.bjfu.exam.entity.export;

import com.bjfu.exam.entity.paper.Paper;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity(name = "exam_paper_answer_export_job")
public class PaperAnswerExportJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 导出任务所属的试卷
     */
    @ManyToOne
    private Paper paper;
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
