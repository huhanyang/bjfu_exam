package com.bjfu.exam.vo.user;

import com.bjfu.exam.vo.paper.PaperVO;
import lombok.Data;

import java.util.List;

@Data
public class UserDetailVO {
    /**
     * 用户id
     */
    private Long id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 账号类型
     */
    private Integer type;
    /**
     * 创建的试卷
     */
    private List<PaperVO> papers;
}
