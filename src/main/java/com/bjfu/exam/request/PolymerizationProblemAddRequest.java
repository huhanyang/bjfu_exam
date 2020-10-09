package com.bjfu.exam.request;

import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class PolymerizationProblemAddRequest extends BaseRequest{
    private Long paperId;
    private String title;
    private String material;

    @Override
    public boolean isComplete() {
        if(paperId == null || StringUtils.isEmpty(title) || StringUtils.isEmpty(material)) {
            return false;
        }
        return true;
    }
}
