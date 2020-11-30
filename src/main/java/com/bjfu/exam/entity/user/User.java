package com.bjfu.exam.entity.user;

import com.bjfu.exam.entity.BaseEntity;
import com.bjfu.exam.entity.answer.PaperAnswer;
import com.bjfu.exam.entity.paper.Paper;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "exam_user", uniqueConstraints = {@UniqueConstraint(columnNames = {"account"})})
public class User extends BaseEntity {
    /**
     * 用户名
     */
    @Column(length=32, nullable=false)
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
    private Integer type;
    /**
     * 账号状态
     */
    @Column(nullable=false)
    private Integer state;

    /**
     * 创建的试卷
     */
    @OneToMany(mappedBy = "creator")
    private List<Paper> papers = new ArrayList<>();
    /**
     * 作答的试卷
     */
    @OneToMany(mappedBy = "user")
    private List<PaperAnswer> paperAnswers = new ArrayList<>();
}
