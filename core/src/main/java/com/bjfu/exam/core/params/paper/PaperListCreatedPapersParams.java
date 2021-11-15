package com.bjfu.exam.core.params.paper;

import com.bjfu.exam.api.enums.PaperStateEnum;
import com.bjfu.exam.core.params.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PaperListCreatedPapersParams extends PageRequest {
    /**
     * 试卷状态
     */
    private PaperStateEnum state;
}
