package com.bjfu.exam.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjfu.exam.entity.answer.PaperAnswer;
import com.bjfu.exam.entity.answer.ProblemAnswer;
import com.bjfu.exam.entity.paper.Paper;
import com.bjfu.exam.entity.paper.PolymerizationProblem;
import com.bjfu.exam.entity.paper.Problem;
import com.bjfu.exam.enums.PaperStateEnum;
import com.bjfu.exam.enums.ResultEnum;
import com.bjfu.exam.exception.BadParamException;
import com.bjfu.exam.exception.NotAllowOperationException;
import com.bjfu.exam.exception.UnauthorizedOperationException;
import com.bjfu.exam.repository.paper.PaperRepository;
import com.bjfu.exam.service.ExportService;
import com.bjfu.exam.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExportServiceImpl implements ExportService {

    @Autowired
    private PaperRepository paperRepository;

    @Override
    @Transactional
    public void exportPaperAnswersToExcel(Long paperId, Long userId, OutputStream outputStream) {
        Optional<Paper> paperOptional = paperRepository.findById(paperId);
        if(paperOptional.isEmpty()) {
            throw new BadParamException(ResultEnum.PAPER_NOT_EXIST);
        }
        Paper paper = paperOptional.get();
        if(!paper.getCreator().getId().equals(userId)) {
            throw new UnauthorizedOperationException(userId, ResultEnum.NOT_CREATOR_EXPORT_PAPER);
        }
        if(!paper.getState().equals(PaperStateEnum.END_ANSWER.getState())) {
            throw new NotAllowOperationException(ResultEnum.PAPER_STATE_IS_NOT_END_ANSWER);
        }
        // 1.试题按照sort排序(考虑组合题目)
        List<Problem> problems = paper.getProblems().stream()
                .sorted(this::problemSort)
                .collect(Collectors.toList());
        // 2.创建表头
        List<List<String>> head = new LinkedList<>();
        // 2.1 答卷人相关
        head.add(Collections.singletonList("答题人账号"));
        head.add(Collections.singletonList("答题人账号姓名"));
        // 2.2 表头试卷收集项
        JSONArray collections =(JSONArray) JSONObject.parse(paper.getCollection());
        collections.forEach(collection -> head.add((Collections.singletonList((String) collection))));
        // 2.3 表头每道题目的相关项
        problems.forEach(problem -> {
            PolymerizationProblem polymerizationProblem = problem.getPolymerizationProblem();
            String problemName;
            if(polymerizationProblem != null) {
                problemName = "第" + polymerizationProblem.getSort() + "题-" + "第" + problem.getSort() + "小题 ";
            } else {
                problemName = "第" + problem.getSort() + "题 ";
            }
            head.add(Collections.singletonList(problemName + "答案"));
            head.add(Collections.singletonList(problemName + "编辑耗时(分钟)"));
            head.add(Collections.singletonList(problemName + "总耗时(分钟)"));
        });
        // 3.填充数据
        List<List<String>> data = new LinkedList<>();
        Set<PaperAnswer> paperAnswers = paper.getPaperAnswers();
        paperAnswers.forEach(paperAnswer -> {
            List<String> row = new LinkedList<>();
            // 3.1答卷人相关
            row.add(paperAnswer.getUser().getAccount());
            row.add(paperAnswer.getUser().getName());
            // 3.2 表头试卷收集项
            JSONObject collectionAnswers = (JSONObject) JSONObject.parse(paperAnswer.getCollectionAnswer());
            collections.forEach(key -> row.add((String) collectionAnswers.get(key)));
            // 3.3 表头每道题目的相关项
            List<ProblemAnswer> problemAnswers =
                    paperAnswer.getProblemAnswers().stream()
                            .sorted((paperAnswer1, paperAnswer2) ->
                                    problemSort(paperAnswer1.getProblem(), paperAnswer2.getProblem()))
                            .collect(Collectors.toList());
            problemAnswers.forEach(problemAnswer -> {
                row.add(problemAnswer.getAnswer());
                row.add(problemAnswer.getEditTime().toString());
                row.add(problemAnswer.getTotalTime().toString());
            });
            data.add(row);
        });
        ExcelUtil.exportToExcel(outputStream, paper.getTitle(),head, data);
    }
    private int problemSort(Problem problem1, Problem problem2) {
        PolymerizationProblem polymerizationProblem1 = problem1.getPolymerizationProblem();
        PolymerizationProblem polymerizationProblem2 = problem2.getPolymerizationProblem();
        if(polymerizationProblem1 == null && polymerizationProblem2 == null) {
            return problem1.getSort() - problem2.getSort();
        } else if(polymerizationProblem1 != null && polymerizationProblem2 != null) {
            if(polymerizationProblem1.getSort().equals(polymerizationProblem2.getSort())) {
                return problem1.getSort() - problem2.getSort();
            } else {
                return polymerizationProblem1.getSort() - polymerizationProblem2.getSort();
            }
        } else if(polymerizationProblem1 == null) {
            return problem1.getSort() - polymerizationProblem2.getSort();
        } else {
            return polymerizationProblem1.getSort() - problem2.getSort();
        }
    }
}
