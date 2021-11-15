package exam.request.paper;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 修改题目请求
 * @author warthog
 */
@Data
public class ProblemEditRequest {

    @NotNull(message = "试题id不能为空!")
    private Long problemId;

    @NotBlank(message = "标题不能为空!")
    @Length(min = 1, max = 64, message = "标题长度在1-64位!")
    private String title;

    @NotBlank(message = "材料不能为空!")
    @Length(min = 1, max = 256, message = "材料长度在1-256位!")
    private String material;

    private String answer;
}
