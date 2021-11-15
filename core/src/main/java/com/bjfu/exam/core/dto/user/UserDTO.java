package com.bjfu.exam.core.dto.user;

import com.bjfu.exam.api.enums.UserStateEnum;
import com.bjfu.exam.api.enums.UserTypeEnum;
import lombok.Data;

import java.util.Date;

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
    private UserTypeEnum type;
    /**
     * 账号状态
     */
    private UserStateEnum state;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 最后修改时间
     */
    private Date lastModifiedTime;
}
