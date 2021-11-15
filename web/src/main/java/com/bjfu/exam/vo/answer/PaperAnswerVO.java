package exam.vo.answer;

import lombok.Data;

import java.util.Date;

@Data
public class PaperAnswerVO {
    /**
     * 答卷id
     */
    private Long id;
    /**
     * 答卷状态
     */
    private Integer state;
    /**
     * 试卷
     */
    private String paperTitle;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 答题结束时间
     */
    private Date finishTime;
}
