package com.bjfu.exam.api.service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 导出相关
 */
public interface ExportService {
    /**
     * 教师导出作答结束试卷的答题信息excel
     */
    void exportPaperAnswersToExcel(Long paperId, Long userId, HttpServletResponse response) throws IOException;
}
