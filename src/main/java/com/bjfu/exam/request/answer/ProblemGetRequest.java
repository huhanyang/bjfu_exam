package com.bjfu.exam.request.answer;

import com.bjfu.exam.request.BaseRequest;
import lombok.Data;

@Data
public class ProblemGetRequest extends BaseRequest {
    private Long paperAnswerId;
    private Integer problemSort;

    @Override
    public boolean isComplete() {
        if(paperAnswerId == null || problemSort == null) {
            return false;
        }
        return true;
    }
}
