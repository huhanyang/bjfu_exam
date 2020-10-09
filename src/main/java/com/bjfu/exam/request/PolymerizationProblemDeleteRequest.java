package com.bjfu.exam.request;

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
