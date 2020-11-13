package com.bjfu.exam.controller;

import com.alibaba.fastjson.JSONObject;
import com.bjfu.exam.enums.ResponseBodyEnum;
import com.bjfu.exam.service.ExportService;
import com.bjfu.exam.vo.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/export")
public class ExportController {

    @Autowired
    private ExportService exportService;

    @GetMapping("/exportPaper")
    public void exportPaper(Long paperId, HttpServletResponse response, HttpSession session) throws IOException {
        if(paperId != null) {
            try {
                response.setContentType("application/vnd.ms-excel");
                response.setCharacterEncoding("utf-8");
                String fileName = URLEncoder.encode("测试", StandardCharsets.UTF_8)
                        .replaceAll("\\+", "%20");
                response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
                exportService.exportPaperAnswersToExcel(paperId, (Long) session.getAttribute("userId"),
                        response.getOutputStream());
            } catch (Exception e) {
                // 重置response
                response.reset();
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                ResponseBody<Void> responseBody = new ResponseBody<>(ResponseBodyEnum.EXPORT_PAPER_FAILED);
                String responseString = JSONObject.toJSON(responseBody).toString();
                response.getWriter().println(responseString);
            }
        } else {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            ResponseBody<Void> responseBody = new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
            String responseString = JSONObject.toJSON(responseBody).toString();
            response.getWriter().println(responseString);
        }
    }

}
