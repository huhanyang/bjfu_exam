package com.bjfu.exam.request.paper;

import com.bjfu.exam.request.BaseRequest;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class ProblemAddRequest extends BaseRequest {
    private Long paperId;
    private Long polymerizationProblemId;
    private String title;
    private String material;
    private Integer type;
    private String answer;

    @Override
    public boolean isComplete() {
        if(paperId == null || StringUtils.isEmpty(title) ||
                StringUtils.isEmpty(material) || type == null || StringUtils.isEmpty(answer)) {
            return false;
        }
        return true;
    }
}
