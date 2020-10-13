package com.bjfu.exam.service;

import com.bjfu.exam.dto.answer.PaperAnswerDTO;
import com.bjfu.exam.dto.paper.ProblemDTO;
import com.bjfu.exam.request.answer.PaperAnswerCreateRequest;

public interface AnswerService {
    PaperAnswerDTO createPaperAnswer(PaperAnswerCreateRequest paperAnswerCreateRequest);
    // todo 获取下一题
    ProblemDTO getNextProblem();
}
