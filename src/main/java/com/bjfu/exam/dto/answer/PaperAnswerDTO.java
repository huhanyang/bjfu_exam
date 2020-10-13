package com.bjfu.exam.dto.answer;

import com.bjfu.exam.dto.paper.PaperDTO;
import com.bjfu.exam.dto.user.UserDTO;
import lombok.Data;

@Data
public class PaperAnswerDTO {
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
     * 总耗时(秒)
     */
    private Integer time;
}
