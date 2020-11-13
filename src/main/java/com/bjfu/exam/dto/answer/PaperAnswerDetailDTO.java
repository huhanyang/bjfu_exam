package com.bjfu.exam.dto.answer;

import com.bjfu.exam.dto.paper.PaperDTO;
import com.bjfu.exam.dto.user.UserDTO;
import lombok.Data;

import java.util.List;

@Data
public class PaperAnswerDetailDTO {
    /**
     * 答卷id
     */
    private Long id;
    /**
     * 答题用户
     */
    private UserDTO user;
    /**
     * 所答试卷
     */
    private PaperDTO paper;
    /**
     * 收集项答案(JSON)
     */
    private String collectionAnswer;
    /**
     * 答卷状态
     */
    private Integer state;
    /**
     * 答卷总用时(秒)
     */
    private Long totalTime;
    /**
     * 题目答案
     */
    List<ProblemAnswerDetailDTO> problemAnswers;
}
