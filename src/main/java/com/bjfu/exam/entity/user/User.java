package com.bjfu.exam.entity.user;

import com.bjfu.exam.entity.answer.PaperAnswer;
import com.bjfu.exam.entity.paper.Paper;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity(name = "exam_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 用户名
     */
    private String account;
    /**
     * 密码
     */
    private String password;
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
    @OneToMany(mappedBy = "creator")
    private Set<Paper> papers = new HashSet<>();
    /**
     * 作答的试卷
     */
    @OneToMany(mappedBy = "user")
    private Set<PaperAnswer> paperAnswers = new HashSet<>();
}
