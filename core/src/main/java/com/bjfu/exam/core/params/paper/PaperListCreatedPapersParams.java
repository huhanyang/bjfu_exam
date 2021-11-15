package com.bjfu.exam.core.params.paper;

import com.bjfu.exam.api.enums.PaperStateEnum;
import com.bjfu.exam.core.params.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PaperListCreatedPaperParams extends PageRequest {
    private PaperStateEnum state;
}
