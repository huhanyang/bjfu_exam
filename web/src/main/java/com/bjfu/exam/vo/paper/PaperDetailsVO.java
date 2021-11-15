package com.bjfu.exam.vo.paper;

import com.bjfu.exam.vo.user.UserVO;
import lombok.Data;

@Data
public class PaperDetailsVO {
    /**
     * 试卷id
     */
    private Long id;
    /**
     * 试卷代号
     */
    private String code;
    /**
     * 创建者
     */
    private UserVO creator;
    /**
     * 试卷状态
     */
    private Integer state;
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
}
