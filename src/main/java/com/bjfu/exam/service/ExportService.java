package com.bjfu.exam.service;

import java.io.OutputStream;

/**
 * 导出相关
 */
public interface ExportService {
    /**
     * 教师导出作答结束试卷的答题信息excel
     */
    void exportPaperAnswersToExcel(Long paperId, Long userId, OutputStream outputStream);
}
