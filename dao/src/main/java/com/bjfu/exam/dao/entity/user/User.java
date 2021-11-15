package com.bjfu.exam.dao.entity.user;

import com.bjfu.exam.api.enums.UserStateEnum;
import com.bjfu.exam.api.enums.UserTypeEnum;
import com.bjfu.exam.dao.entity.BaseEntity;
import com.bjfu.exam.dao.entity.paper.Paper;
import com.bjfu.exam.dao.entity.answer.PaperAnswer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "exam_user")
public class User extends BaseEntity {
    /**
     * 用户名
     */
    @Column(length=32, nullable=false, unique = true)
    private String account;
    /**
     * 密码
     */
    @Column(length=32, nullable=false)
    private String password;
    /**
     * 姓名
     */
    @Column(length=32, nullable=false)
    private String name;
    /**
     * 账号类型
     */
    @Column(nullable=false)
    private UserTypeEnum type;
    /**
     * 账号状态
     */
    @Column(nullable=false)
    private UserStateEnum state;

    /**
     * 创建的试卷
     */
    @OneToMany(mappedBy = "creator")
    @OrderBy(value = "createdTime")
    private List<Paper> papers = new ArrayList<>();
    /**
     * 作答的试卷
     */
    @OneToMany(mappedBy = "user")
    @OrderBy(value = "createdTime")
    private List<PaperAnswer> paperAnswers = new ArrayList<>();
}
