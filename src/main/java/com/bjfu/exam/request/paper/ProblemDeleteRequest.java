package com.bjfu.exam.request.paper;


import com.bjfu.exam.request.BaseRequest;
import lombok.Data;

@Data
public class ProblemDeleteRequest extends BaseRequest {
    private Long paperId;
    private Long problemId;

    @Override
    public boolean isComplete() {
        if(paperId == null || problemId == null) {
            return false;
        }
        return true;
    }
}
