package exam.request.paper;


import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class ImageInProblemDeleteRequest {

    @NotNull(message = "题目id不能为空!")
    private Long problemId;

    @NotNull(message = "要删除图片的位置不能为空!")
    @Min(value = 0, message = "删除图片的位置小于0!")
    private Integer index;

}
