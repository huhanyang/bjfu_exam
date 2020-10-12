package com.bjfu.exam.request.paper;

import com.bjfu.exam.request.BaseRequest;
import lombok.Data;

@Data
public class PolymerizationProblemDeleteRequest extends BaseRequest {
    private Long paperId;
    private Long polymerizationProblemId;

    @Override
    public boolean isComplete() {
        if(paperId == null || polymerizationProblemId == null) {
            return false;
        }
        return true;
    }
}
