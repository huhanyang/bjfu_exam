package com.bjfu.exam.core.dto.paper;

import com.bjfu.exam.api.enums.PaperStateEnum;
import com.bjfu.exam.core.dto.user.UserDTO;
import lombok.Data;

@Data
public class PaperDTO {
    /**
     * 创建者
     */
    private UserDTO creator;
    /**
     * 试卷代号
     */
    private String code;
    /**
     * 试卷状态
     */
    private PaperStateEnum state;
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
     * 试卷收集项(JSON数组)
     */
    private String collection;
}
