package com.bjfu.exam.request.paper;

import com.bjfu.exam.request.BaseRequest;
import lombok.Data;

import java.util.Map;

@Data
public class ProblemsInPaperResortRequest extends BaseRequest {
    private Long paperId;
    /**
     * 原题号到新题号的转换
     * key:原题号 value:新题号
     */
    private Map<Integer ,Integer> oldIndexToNewIndexMap;

    @Override
    public boolean isComplete() {
        if(paperId == null || oldIndexToNewIndexMap == null || oldIndexToNewIndexMap.isEmpty()) {
            return false;
        }
        return true;
    }
}
