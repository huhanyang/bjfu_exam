package com.bjfu.exam.service;

import com.bjfu.exam.dto.answer.PaperAnswerDTO;
import com.bjfu.exam.dto.answer.PaperAnswerDetailDTO;
import com.bjfu.exam.dto.answer.ProblemAnswerDTO;
import com.bjfu.exam.dto.paper.PolymerizationProblemDetailDTO;
import com.bjfu.exam.dto.paper.ProblemDTO;
import com.bjfu.exam.request.answer.PaperAnswerCreateRequest;
import com.bjfu.exam.request.answer.ProblemAnswerSubmitRequest;

import java.util.List;

/**
 * 作答相关
 */
public interface AnswerService {
    /**
     * 获取创建的试题试卷
     */
    List<PaperAnswerDetailDTO> getPaperAnswers(Long userId);
    /**
     * 获取试题试卷详情
     */
    PaperAnswerDetailDTO getPaperAnswerDetail(Long userId, Long paperAnswerId);
    /**
     * 创建答题试卷
     */
    PaperAnswerDTO createPaperAnswer(Long userId, PaperAnswerCreateRequest paperAnswerCreateRequest);
    /**
     * 获取题目
     */
    ProblemDTO getNextProblem(Long userId, Long paperAnswerId);
    /**
     * 提交单个题目答案
     */
    ProblemAnswerDTO submitAnswer(Long userId, ProblemAnswerSubmitRequest problemAnswerSubmitRequest);
}
