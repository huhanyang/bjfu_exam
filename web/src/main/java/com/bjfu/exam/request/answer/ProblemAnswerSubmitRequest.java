package exam.request.answer;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Date;

@Data
public class ProblemAnswerSubmitRequest {

    @NotNull(message = "答卷id不能为空!")
    private Long paperAnswerId;

    @NotNull(message = "开始作答时间不能为空!")
    @Past(message = "开始作答时间必须为过去时间!")
    private Date startTime;

    @NotNull(message = "第一次编辑时间不能为空!")
    @Past(message = "第一次编辑时间必须为过去时间!")
    private Date firstEditTime;

    @NotNull(message = "编辑时间不能为空!")
    @Min(value = 0, message = "编辑时间必须大于0秒!")
    private Integer editTime;

    @NotNull(message = "答案提交时间不能为空!")
    @Past(message = "答案提交时间必须为过去时间!")
    private Date submitTime;

    @NotBlank(message = "试题答案不能为空!")
    @Length(min = 1, max = 256, message = "答案长度在1-512位!")
    private String answer;

}
