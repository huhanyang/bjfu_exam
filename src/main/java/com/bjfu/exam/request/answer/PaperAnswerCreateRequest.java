package com.bjfu.exam.request.answer;

import com.alibaba.fastjson.JSONObject;
import com.bjfu.exam.request.BaseRequest;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class PaperAnswerCreateRequest extends BaseRequest {
    /**
     * 试卷id
     */
    Long paperId;
    /**
     * 收集项答案(JSON)
     */
    private String collectionAnswer;

    @Override
    public boolean isComplete() {
        if(paperId == null || StringUtils.isEmpty(collectionAnswer)) {
            return false;
        }
        try {
            JSONObject.parse(collectionAnswer);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
