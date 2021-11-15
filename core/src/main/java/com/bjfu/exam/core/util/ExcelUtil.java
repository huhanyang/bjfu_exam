package com.bjfu.exam.core.util;

import com.alibaba.excel.EasyExcel;

import java.io.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ExcelUtil {

    public static void exportToExcel(OutputStream outputStream, String sheetName, List<List<String>> head, List<List<String>> data) {
        EasyExcel.write(outputStream).autoCloseStream(Boolean.FALSE).head(head).sheet(sheetName).doWrite(data);
    }

    public static void main(String[] args) throws IOException {
        File file = new File("D:\\test.xlsx");
        List<List<String>> head = new LinkedList<>();
        List<List<String>> data = new LinkedList<>();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        for (int i = 0; i< 10 ; i++) {
            head.add(Collections.singletonList("第"+i+"个头"));
        }
        for (int i = 0; i< 10 ; i++) {
            List<String> row = new LinkedList<>();
            for (int j = 0; j< 10 ; j++) {
                row.add("第"+j+"个数据");
            }
            data.add(row);
        }
        exportToExcel(fileOutputStream, "测试sheet", head, data);
        fileOutputStream.close();
    }
}
