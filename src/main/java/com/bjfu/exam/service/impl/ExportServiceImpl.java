package com.bjfu.exam.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjfu.exam.entity.answer.PaperAnswer;
import com.bjfu.exam.entity.answer.ProblemAnswer;
import com.bjfu.exam.entity.paper.Paper;
import com.bjfu.exam.entity.paper.Problem;
import com.bjfu.exam.enums.PaperAnswerStateEnum;
import com.bjfu.exam.enums.PaperStateEnum;
import com.bjfu.exam.enums.ProblemTypeEnum;
import com.bjfu.exam.enums.ResultEnum;
import com.bjfu.exam.exception.BadParamExceptionExam;
import com.bjfu.exam.exception.NotAllowOperationExceptionExam;
import com.bjfu.exam.exception.UnauthorizedOperationExceptionExam;
import com.bjfu.exam.repository.paper.PaperRepository;
import com.bjfu.exam.service.ExportService;
import com.bjfu.exam.util.DateUtil;
import com.bjfu.exam.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExportServiceImpl implements ExportService {

    @Autowired
    private PaperRepository paperRepository;

    @Override
    @Transactional
    public void exportPaperAnswersToExcel(Long paperId, Long userId, HttpServletResponse response) throws IOException {
        Optional<Paper> paperOptional = paperRepository.findById(paperId);
        if(paperOptional.isEmpty()) {
            throw new BadParamExceptionExam(ResultEnum.PAPER_NOT_EXIST);
        }
        Paper paper = paperOptional.get();
        if(!paper.getCreator().getId().equals(userId)) {
            throw new UnauthorizedOperationExceptionExam(userId, ResultEnum.NOT_CREATOR_EXPORT_PAPER);
        }
        if(!paper.getState().equals(PaperStateEnum.END_ANSWER.getState())) {
            throw new NotAllowOperationExceptionExam(ResultEnum.PAPER_STATE_IS_NOT_END_ANSWER);
        }
        String fileName = URLEncoder.encode(paper.getTitle(), StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        // 1.创建表头
        List<List<String>> head = new LinkedList<>();
        // 1.1 表头答卷相关
        head.add(Collections.singletonList("答题人账号"));
        head.add(Collections.singletonList("答题人账号姓名"));
        head.add(Collections.singletonList("开始答题时间"));
        head.add(Collections.singletonList("结束答题时间"));
        head.add(Collections.singletonList("答题状态"));
        // 1.2 表头的试卷收集项
        JSONArray collections =(JSONArray) JSONObject.parse(paper.getCollection());
        collections.forEach(collection -> head.add((Collections.singletonList((String) collection))));
        // 1.3 表头中每道题目
        head.addAll(getProblemsHeadList(paper));
        // 2.填充数据
        List<List<String>> data = new LinkedList<>();
        List<PaperAnswer> paperAnswers = sortPaperAnswer(paper.getPaperAnswers());
        paperAnswers.forEach(paperAnswer -> {
            List<String> row = new LinkedList<>();
            // 2.1 答卷相关
            row.add(paperAnswer.getUser().getAccount());
            row.add(paperAnswer.getUser().getName());
            row.add(Optional.ofNullable(paperAnswer.getCreatedTime()).map(Object::toString).orElse(""));
            row.add(Optional.ofNullable(paperAnswer.getFinishTime()).map(Object::toString).orElse(""));
            if(paperAnswer.getState().equals(PaperAnswerStateEnum.OVERTIME.getState())) {
                row.add("作答时间超时");
            } else if(paperAnswer.getState().equals(PaperAnswerStateEnum.FINISH.getState())) {
                row.add("作答完成");
            } else {
                row.add("作答中");
            }
            // 2.2 表头试卷收集项
            JSONObject collectionAnswers = (JSONObject) JSONObject.parse(paperAnswer.getCollectionAnswer());
            collections.forEach(key -> row.add((String) collectionAnswers.get(key)));
            // 2.3 表头每道题目的相关项
            // todo 这里导出不出错的前提是题目答案提交是按照顺序来的,否则请按照试题序号排序
            List<ProblemAnswer> problemAnswers = paperAnswer.getProblemAnswers();
            problemAnswers.forEach(problemAnswer -> {
                row.add(problemAnswer.getAnswer());
                if(problemAnswer.getProblem().getType().equals(ProblemTypeEnum.MATERIAL_PROBLEM.getType())) {
                    row.add(DateUtil.calLastedTime(problemAnswer.getStartTime(), problemAnswer.getFirstEditTime()).toString());
                    row.add(problemAnswer.getEditTime().toString());
                }
                row.add(DateUtil.calLastedTime(problemAnswer.getStartTime(), problemAnswer.getSubmitTime()).toString());
                row.add(problemAnswer.getStartTime().toString());
                row.add(problemAnswer.getSubmitTime().toString());
            });
            data.add(row);
        });
        ExcelUtil.exportToExcel(response.getOutputStream(), paper.getTitle(), head, data);
    }

    /**
     * 获取试卷导出时所有试题的表头
     */
    private List<List<String>> getProblemsHeadList(Paper paper) {
        List<Problem> bigProblems = paper.getProblems().stream()
                .filter(problem -> problem.getFatherProblem() == null)
                .collect(Collectors.toList());
        LinkedList<List<String>> list = new LinkedList<>();
        for(Problem bigProblem: bigProblems) {
            final String problemName = "第"+bigProblem.getSort()+"题";
            if(bigProblem.getType().equals(ProblemTypeEnum.FATHER_PROBLEM.getType())) {
                List<Problem> subProblems = bigProblem.getSubProblems();
                subProblems.forEach(subProblem -> {
                    list.addAll(getProblemHeadList(problemName+"-第"+subProblem.getSort()+"小题\n", subProblem.getType()));
                });
            } else {
                list.addAll(getProblemHeadList(problemName + "\n", bigProblem.getType()));
            }
        }
        return list;
    }

    /**
     * 获取一道题的表头
     */
    private List<List<String>> getProblemHeadList(String problemName, int problemType) {
        List<List<String>> list = new ArrayList<>();
        list.add(Collections.singletonList(problemName + "答案"));
        if(problemType == ProblemTypeEnum.MATERIAL_PROBLEM.getType()) {
            list.add(Collections.singletonList(problemName + "思考时间(秒)"));
            list.add(Collections.singletonList(problemName + "编辑时间(秒)"));
        }
        list.add(Collections.singletonList(problemName + "总用时(秒)"));
        list.add(Collections.singletonList(problemName + "开始时间"));
        list.add(Collections.singletonList(problemName + "提交时间"));
        return list;
    }

    /**
     * 试卷排序逻辑
     * 可导出前提前按照逻辑进行排序
     */
    private List<PaperAnswer> sortPaperAnswer(List<PaperAnswer> paperAnswers) {
        return paperAnswers;
    }

}
