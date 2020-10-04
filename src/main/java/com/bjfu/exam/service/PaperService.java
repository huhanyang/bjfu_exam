package com.bjfu.exam.service;

import com.bjfu.exam.entity.paper.Paper;
import com.bjfu.exam.entity.paper.PolymerizationProblem;
import com.bjfu.exam.entity.paper.Problem;
import com.bjfu.exam.request.PaperCreateRequest;
import com.bjfu.exam.request.PolymerizationProblemAddRequest;
import com.bjfu.exam.request.ProblemAddRequest;

import java.io.File;

public interface PaperService {
    /**
     * 根据试卷代码获取试卷
     */
    Paper getPaper(String code);
    /**
     * 创建试卷(未创建题目)
     */
    Paper createPaper(PaperCreateRequest paperCreateRequest, Long creatorId);
    /**
     * 为试卷添加组合题目
     */
    PolymerizationProblem addPolymerizationProblemInPaper(Long userId,
                                                          PolymerizationProblemAddRequest polymerizationProblemAddRequest);
    /**
     * 为组合题目添加图片
     */
    PolymerizationProblem addImageInPolymerizationProblem(Long userId, Long polymerizationProblemId, File image);
    /**
     * 添加题目
     */
    Problem addProblem(Long userId, ProblemAddRequest problemAddRequest);
    /**
     * 为题目添加图片
     */
    Problem addImageInProblem(Long userId, Long problemId, File image);
    /**
     * 删除题目
     */
    Paper deleteProblem(Long userId, Long paperId, Long problemId);
    /**
     * 删除组合题目
     */
    Paper deletePolymerizationProblem(Long userId, Long paperId, Long polymerizationProblemId);
    /**
     * 删除试卷
     */
    void deletePaper(Long userId, Long paperId);
}
