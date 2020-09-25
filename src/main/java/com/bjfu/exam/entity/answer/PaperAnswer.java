package com.bjfu.exam.entity.answer;

import com.bjfu.exam.entity.user.User;
import com.bjfu.exam.entity.paper.Paper;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name = "exam_paper_answer")
public class PaperAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 答题用户
     */
    @ManyToOne
    private User user;
    /**
     * 所答试卷
     */
    @ManyToOne
    private Paper paper;
    /**
     * 收集项答案(JSON)
     */
    private String collectionAnswer;
    /**
     * 总耗时(秒)
     */
    private Integer time;

    @OneToMany(mappedBy = "paperAnswer")
    Set<ProblemAnswer> problemAnswers = new HashSet<>();
}
