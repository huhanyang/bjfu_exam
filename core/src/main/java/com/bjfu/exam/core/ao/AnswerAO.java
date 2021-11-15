package com.bjfu.exam.core.ao;

import com.bjfu.exam.core.dto.answer.PaperAnswerDTO;
import com.bjfu.exam.core.dto.answer.ProblemDTO;
import com.bjfu.exam.core.params.answer.AnswerCreatePaperAnswerParams;
import com.bjfu.exam.core.params.answer.AnswerCreateProblemAnswerParams;

public interface AnswerAO {
    PaperAnswerDTO createPaperAnswer(AnswerCreatePaperAnswerParams params, Long operatorId);

    ProblemDTO createProblemAnswer(AnswerCreateProblemAnswerParams params, Long operatorId);

    ProblemDTO getNextProblem(Long paperId, Long operatorId);
}
