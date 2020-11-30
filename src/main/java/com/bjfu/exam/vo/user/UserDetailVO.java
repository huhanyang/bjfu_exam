package com.bjfu.exam.vo.user;

import lombok.Data;

import java.util.Date;

@Data
public class UserDetailVO {
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
     * 用户状态
     */
    private Integer state;
    /**
     * 创建时间
     */
    private Date createdTime;
}
