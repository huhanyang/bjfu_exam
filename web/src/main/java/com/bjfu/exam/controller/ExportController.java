package com.bjfu.exam.controller;

import com.alibaba.fastjson.JSONObject;
import com.bjfu.exam.vo.BaseResult;
import com.bjfu.exam.enums.ResultEnum;
import com.bjfu.exam.security.annotation.RequireTeacher;
import com.bjfu.exam.service.ExportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@RequestMapping("/export")
@Slf4j
public class ExportController {

    @Autowired
    private ExportService exportService;

    @GetMapping("/exportPaper")
    @RequireTeacher
    public void exportPaper(@NotNull(message = "试卷id不能为空!") Long paperId, HttpServletResponse response, HttpSession session) throws IOException {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            exportService.exportPaperAnswersToExcel(paperId, (Long) session.getAttribute("userId"),
                    response);
        } catch (Exception e) {
            log.error("export excel failed!", e);
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            BaseResult<Void> baseResult = new BaseResult<>(ResultEnum.EXPORT_PAPER_FAILED);
            String responseString = JSONObject.toJSON(baseResult).toString();
            response.getWriter().println(responseString);
        }
    }

}
