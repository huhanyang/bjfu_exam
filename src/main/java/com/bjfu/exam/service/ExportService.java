package com.bjfu.exam.service;

import java.io.OutputStream;

/**
 * 导出相关
 */
public interface ExportService {
    void exportPaperAnswersToExcel(Long paperId, Long userId, OutputStream outputStream);
}
