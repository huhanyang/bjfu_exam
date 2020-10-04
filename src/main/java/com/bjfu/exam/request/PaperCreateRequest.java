package com.bjfu.exam.request;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class PaperCreateRequest extends BaseRequest {
    /**
     * 试卷标题
     */
    private String title;
    /**
     * 试卷简介
     */
    private String introduction;
    /**
     * 最长答题时间(分钟)
     */
    private Integer time;
    /**
     * 试卷收集项json格式
     */
    private String collection;

    @Override
    public boolean isComplete() {
        if(StringUtils.isEmpty(title) || StringUtils.isEmpty(introduction)
                || time == null ||StringUtils.isEmpty(collection)) {
            try {
                JSONObject.toJSON(collection);
                return true;
            } catch (Exception ignored) {
            }
        }
        return false;
    }
}
