package com.bjfu.exam.request.paper;

import com.bjfu.exam.request.BaseRequest;
import lombok.Data;

@Data
public class ImageInPolymerizationProblemDeleteRequest extends BaseRequest {
    /**
     * 组合题目id
     */
    private Long polymerizationProblemId;
    /**
     * 删除的图片
     */
    private Integer index;

    @Override
    public boolean isComplete() {
        if(polymerizationProblemId == null || index == null) {
            return false;
        }
        return true;
    }
}
