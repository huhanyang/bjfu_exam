package com.bjfu.exam.dto.answer;

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
     * 创建时间
     */
    private Date createdTime;
    /**
     * 答题结束时间
     */
    private Date finishTime;
}
