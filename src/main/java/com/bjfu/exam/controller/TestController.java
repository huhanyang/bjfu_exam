package com.bjfu.exam.controller;

import com.bjfu.exam.dto.PaperDTO;
import com.bjfu.exam.entity.paper.Paper;
import com.bjfu.exam.repository.paper.PaperRepository;
import com.bjfu.exam.service.PaperService;
import com.bjfu.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private UserService userService;
    @Autowired
    private PaperService paperService;
    @Autowired
    private PaperRepository paperRepository;

    @GetMapping("/test")
    public PaperDTO test() {
        PaperDTO paper = paperService.getPaper("code");
        return paper;
    }
}
