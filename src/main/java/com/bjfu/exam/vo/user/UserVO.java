package com.bjfu.exam.vo.user;

import lombok.Data;

@Data
public class UserVO {
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
}
