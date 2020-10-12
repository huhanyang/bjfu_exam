package com.bjfu.exam.request.paper;

import com.bjfu.exam.request.BaseRequest;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageInProblemAddRequest extends BaseRequest {
    /**
     * 题目id
     */
    private Long problemId;
    /**
     * 插入的位置
     */
    private Integer index;
    /**
     * 要添加的图片
     */
    private MultipartFile imgFile;

    @Override
    public boolean isComplete() {
        if(problemId == null || index == null || imgFile.isEmpty()) {
            return false;
        }
        return true;
    }
}
