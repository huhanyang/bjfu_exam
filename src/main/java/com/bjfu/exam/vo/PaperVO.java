package com.bjfu.exam.vo;

import com.bjfu.exam.entity.user.User;
import lombok.Data;

@Data
public class PaperVO {
    private Long id;
    private String title;
    private String introduction;
    private Integer time;
    private User creator;
    private int status;
}
