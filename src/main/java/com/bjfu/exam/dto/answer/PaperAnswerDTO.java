package com.bjfu.exam.dto.answer;

import com.bjfu.exam.dto.paper.PaperDTO;
import lombok.Data;

import java.util.Date;


@Data
public class PaperAnswerDTO {
    /**
     * 答卷id
     */
    private Long id;
    /**
     * 答卷状态
     */
    private Integer state;
    /**
     * 试卷
     */
    private String paperTitle;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 答题结束时间
     */
    private Date finishTime;
}
