package exam.request.paper;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class ImageInProblemAddRequest {

    @NotNull(message = "题目id不能为空!")
    private Long problemId;

    @NotNull(message = "图片插入的位置不能为空!")
    @Min(value = 0, message = "图片插入的位置小于0!")
    private Integer index;

    @NotNull(message = "图片文件不能为空!")
    private MultipartFile imgFile;

}
