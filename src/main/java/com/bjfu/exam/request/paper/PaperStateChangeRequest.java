package com.bjfu.exam.request.paper;

import com.bjfu.exam.request.BaseRequest;
import lombok.Data;

@Data
public class PaperStateChangeRequest extends BaseRequest {
    private Long paperId;
    private Integer state;

    @Override
    public boolean isComplete() {
        if(paperId == null || state == null) {
            return false;
        }
        return true;
    }
}
