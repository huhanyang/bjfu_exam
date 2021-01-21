package com.bjfu.exam.vo.paper;

import com.bjfu.exam.vo.user.UserVO;
import lombok.Data;

@Data
public class PaperVO {
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
     * 总答题人数
     */
    private Integer paperAnswerCount;
    /**
     * 答题完成的人数
     */
    private Integer finishedPaperAnswerCount;
}
