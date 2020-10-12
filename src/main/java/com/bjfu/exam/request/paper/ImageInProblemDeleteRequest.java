package com.bjfu.exam.request.paper;


import com.bjfu.exam.request.BaseRequest;
import lombok.Data;

@Data
public class ImageInProblemDeleteRequest extends BaseRequest {

    /**
     * 题目id
     */
    private Long problemId;
    /**
     * 删除的图片
     */
    private Integer index;

    @Override
    public boolean isComplete() {
        if(problemId == null || index == null) {
            return false;
        }
        return true;
    }
}
