package exam.request.paper;


import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ProblemDeleteRequest {

    @NotNull(message = "试卷id不能为空!")
    private Long paperId;

    @NotNull(message = "试题id不能为空!")
    private Long problemId;

}
