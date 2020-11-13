package com.bjfu.exam.vo.answer;

import com.bjfu.exam.vo.paper.PaperVO;
import com.bjfu.exam.vo.user.UserVO;
import lombok.Data;

import java.util.List;


@Data
public class PaperAnswerDetailVO {
    /**
     * 答卷id
     */
    private Long id;
    /**
     * 答题用户
     */
    private UserVO user;
    /**
     * 所答试卷
     */
    private PaperVO paper;
    /**
     * 收集项答案(JSON)
     */
    private String collectionAnswer;
    /**
     * 答卷总用时(秒)
     */
    private Long totalTime;
    /**
     * 答卷状态
     */
    private Integer state;
    /**
     * 题目答案
     */
    List<ProblemAnswerDetailVO> problemAnswers;
}
