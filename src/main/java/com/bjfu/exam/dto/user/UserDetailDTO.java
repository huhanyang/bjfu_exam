package com.bjfu.exam.dto.user;

import com.bjfu.exam.dto.paper.PaperDTO;
import lombok.Data;

import java.util.List;

@Data
public class UserDetailDTO {
    /**
     * 用户id
     */
    private Long id;
    /**
     * 用户名
     */
    private String account;
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
    private List<PaperDTO> papers;
}
