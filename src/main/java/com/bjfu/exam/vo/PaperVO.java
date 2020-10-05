package com.bjfu.exam.vo;

import lombok.Data;

@Data
public class PaperVO {
    private String code;
    private String title;
    private String introduction;
    private Integer time;
    private String creatorName;
    private int status;
}
