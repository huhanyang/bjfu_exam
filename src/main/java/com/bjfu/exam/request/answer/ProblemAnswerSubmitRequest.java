package com.bjfu.exam.request.answer;

import com.bjfu.exam.request.BaseRequest;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class ProblemAnswerSubmitRequest extends BaseRequest {
    /**
     * 试卷id
     */
    private Long paperAnswerId;
    /**
     * 题目总耗时
     */
    private Integer totalTime;
    /**
     * 修改作答耗时
     */
    private Integer editTime;
    /**
     * 答案
     */
    private String answer;

    @Override
    public boolean isComplete() {
        if(paperAnswerId == null || totalTime == null
                || editTime == null || StringUtils.isEmpty(answer)) {
            return false;
        }
        if(editTime > totalTime) {
            return false;
        }
        return true;
    }
}
