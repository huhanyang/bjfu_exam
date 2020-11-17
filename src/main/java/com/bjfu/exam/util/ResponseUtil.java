package com.bjfu.exam.util;

import com.alibaba.fastjson.JSONObject;
import com.bjfu.exam.enums.ResultEnum;
import com.bjfu.exam.vo.BaseResult;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseUtil {
    public static void writeResultToResponse(ResultEnum resultEnum, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        BaseResult<Void> result = new BaseResult<>(resultEnum);
        writer.println(JSONObject.toJSON(result));
    }
}
