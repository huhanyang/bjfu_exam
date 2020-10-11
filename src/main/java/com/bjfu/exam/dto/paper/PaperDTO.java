package com.bjfu.exam.dto.paper;

import com.bjfu.exam.dto.user.UserDTO;
import lombok.Data;

@Data
public class PaperDTO {
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
    private UserDTO creator;
    /**
     * 试卷状态
     */
    private int status;
}
