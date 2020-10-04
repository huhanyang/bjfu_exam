package com.bjfu.exam.controller;

import com.alibaba.fastjson.JSONObject;
import com.bjfu.exam.entity.paper.Paper;
import com.bjfu.exam.entity.paper.PolymerizationProblem;
import com.bjfu.exam.entity.user.User;
import com.bjfu.exam.enums.UserTypeEnum;
import com.bjfu.exam.repository.paper.PaperRepository;
import com.bjfu.exam.request.PaperCreateRequest;
import com.bjfu.exam.request.PolymerizationProblemAddRequest;
import com.bjfu.exam.request.UserRegisterRequest;
import com.bjfu.exam.service.PaperService;
import com.bjfu.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
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
    public Paper test() {
        Paper paper = paperService.getPaper("code");
        return paper;
    }
}
