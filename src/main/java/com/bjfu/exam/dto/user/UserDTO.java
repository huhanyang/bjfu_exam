package com.bjfu.exam.dto.user;

import lombok.Data;

@Data
public class UserDTO {
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
}
