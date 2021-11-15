package com.bjfu.exam.core.ao.impl;

import com.bjfu.exam.api.enums.ResultEnum;
import com.bjfu.exam.core.ao.AnswerAO;
import com.bjfu.exam.core.dto.answer.PaperAnswerDTO;
import com.bjfu.exam.core.dto.answer.ProblemDTO;
import com.bjfu.exam.core.exception.BizException;
import com.bjfu.exam.core.params.answer.AnswerCreatePaperAnswerParams;
import com.bjfu.exam.core.params.answer.AnswerCreateProblemAnswerParams;
import com.bjfu.exam.dao.entity.paper.Paper;
import com.bjfu.exam.dao.entity.user.User;
import com.bjfu.exam.dao.repository.answer.PaperAnswerRepository;
import com.bjfu.exam.dao.repository.answer.ProblemAnswerRepository;
import com.bjfu.exam.dao.repository.paper.PaperRepository;
import com.bjfu.exam.dao.repository.paper.ProblemRepository;
import com.bjfu.exam.dao.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.bjfu.exam.api.enums.PaperStateEnum.SOFT_DELETE;

@Component
public class AnswerAOImpl implements AnswerAO {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PaperRepository paperRepository;
    @Autowired
    private ProblemRepository problemRepository;
    @Autowired
    private PaperAnswerRepository paperAnswerRepository;
    @Autowired
    private ProblemAnswerRepository problemAnswerRepository;

    @Override
    public PaperAnswerDTO createPaperAnswer(AnswerCreatePaperAnswerParams params, Long operatorId) {
        // 查找用户
        User creator = userRepository.findById(operatorId)
                .orElseThrow(() -> new BizException(ResultEnum.USER_NOT_EXIST));
        // 查找试卷
        Paper paper = getPaper(params.getPaperId());

        return null;
    }

    @Override
    public ProblemDTO createProblemAnswer(AnswerCreateProblemAnswerParams params, Long operatorId) {
        return null;
    }

    @Override
    public ProblemDTO getNextProblem(Long paperId, Long operatorId) {
        return null;
    }

    private Paper getPaperWithAnsweringState(Long paperId) {
        return paperRepository.findById(paperId)
                .filter(paper -> !paper.getState().equals(SOFT_DELETE))
                .orElseThrow(() -> new BizException(ResultEnum.PAPER_NOT_EXIST));
    }
}
