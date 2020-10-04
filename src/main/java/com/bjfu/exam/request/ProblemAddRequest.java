package com.bjfu.exam.request;

import lombok.Data;

@Data
public class ProblemAddRequest {
    private Long paperId;
    private Long polymerizationProblemId;
    private Integer sort;
    private String title;
    private String material;
    private Integer type;
    private String answer;
}
