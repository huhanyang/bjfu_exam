package com.bjfu.exam.dto;

import lombok.Data;

@Data
public class UserDTO {
    /**
     * id
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
