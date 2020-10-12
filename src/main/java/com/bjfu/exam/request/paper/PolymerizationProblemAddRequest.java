package com.bjfu.exam.request.paper;

import com.bjfu.exam.request.BaseRequest;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class PolymerizationProblemAddRequest extends BaseRequest {
    private Long paperId;
    private String title;
    private String material;
    // todo 增加添加位置

    @Override
    public boolean isComplete() {
        if(paperId == null || StringUtils.isEmpty(title) || StringUtils.isEmpty(material)) {
            return false;
        }
        return true;
    }
}
