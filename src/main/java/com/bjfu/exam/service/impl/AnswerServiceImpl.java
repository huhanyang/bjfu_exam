package com.bjfu.exam.service.impl;

import com.bjfu.exam.dto.answer.PaperAnswerDTO;
import com.bjfu.exam.dto.paper.ProblemDTO;
import com.bjfu.exam.entity.answer.PaperAnswer;
import com.bjfu.exam.entity.answer.ProblemAnswer;
import com.bjfu.exam.entity.paper.Paper;
import com.bjfu.exam.entity.paper.Problem;
import com.bjfu.exam.entity.user.User;
import com.bjfu.exam.enums.PaperAnswerStateEnum;
import com.bjfu.exam.enums.PaperStateEnum;
import com.bjfu.exam.enums.ResultEnum;
import com.bjfu.exam.exception.*;
import com.bjfu.exam.repository.answer.PaperAnswerRepository;
import com.bjfu.exam.repository.answer.ProblemAnswerRepository;
import com.bjfu.exam.repository.paper.PaperRepository;
import com.bjfu.exam.repository.paper.ProblemRepository;
import com.bjfu.exam.repository.user.UserRepository;
import com.bjfu.exam.request.answer.PaperAnswerCreateRequest;
import com.bjfu.exam.request.answer.ProblemAnswerSubmitRequest;
import com.bjfu.exam.service.AnswerService;
import com.bjfu.exam.util.DateUtil;
import com.bjfu.exam.util.EntityConvertToDTOUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private PaperAnswerRepository paperAnswerRepository;
    @Autowired
    private ProblemAnswerRepository problemAnswerRepository;
    @Autowired
    private PaperRepository paperRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProblemRepository problemRepository;

    @Override
    public List<PaperAnswerDTO> getPaperAnswers(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()) {
            throw new UserNotExistException(userId, ResultEnum.USER_NOT_EXIST);
        }
        User user = userOptional.get();
        List<PaperAnswer> paperAnswers = paperAnswerRepository.findAllByUser(user);
        return paperAnswers.stream()
                .map(EntityConvertToDTOUtil::convertPaperAnswer)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PaperAnswerDTO createPaperAnswer(Long userId, PaperAnswerCreateRequest paperAnswerCreateRequest) {
        // 针对此用户加锁
        Optional<User> userOptional = userRepository.findByIdForUpdate(userId);
        if(userOptional.isEmpty()) {
            throw new UserNotExistException(userId, ResultEnum.USER_NOT_EXIST);
        }
        Optional<Paper> paperOptional = paperRepository.findById(paperAnswerCreateRequest.getPaperId());
        if(paperOptional.isEmpty()) {
            throw new BadParamException(ResultEnum.PAPER_NOT_EXIST);
        }
        Paper paper = paperOptional.get();
        User user = userOptional.get();
        // 判断此试卷当前是否可以作答
        if(!paper.getState().equals(PaperStateEnum.ANSWERING.getState()) &&
                !paper.getState().equals(PaperStateEnum.READY_TO_ANSWERING.getState())) {
            throw new NotAllowOperationException(ResultEnum.PAPER_STATE_NOT_ANSWERING);
        }
        // 判断是否已经作答过此试卷
        if(paperAnswerRepository.findByUserAndPaper(user, paper).isPresent()) {
            throw new NotAllowOperationException(ResultEnum.ANSWER_TWICE);
        }
        // 更新试卷状态为作答中
        if(paper.getState().equals(PaperStateEnum.READY_TO_ANSWERING.getState())) {
            paper.setState(PaperStateEnum.ANSWERING.getState());
            paperRepository.save(paper);
        }
        PaperAnswer paperAnswer = new PaperAnswer();
        paperAnswer.setUser(user);
        paperAnswer.setPaper(paper);
        paperAnswer.setState(PaperAnswerStateEnum.ANSWERING.getState());

        List<Problem> problems = problemRepository.findByPaperAndSort(paper, 1);
        Optional<Problem> nextProblemOptional = problems.stream()
                .filter(problem -> problem.getFatherProblem() == null)
                .findFirst();
        if(nextProblemOptional.isPresent()) {
            paperAnswer.setNextProblem(nextProblemOptional.get());
        } else {
            paperAnswer.setState(PaperAnswerStateEnum.FINISH.getState());
        }
        BeanUtils.copyProperties(paperAnswerCreateRequest, paperAnswer);
        paperAnswer = paperAnswerRepository.save(paperAnswer);
        return EntityConvertToDTOUtil.convertPaperAnswer(paperAnswer);
    }

    @Override
    public ProblemDTO getNextProblem(Long userId, Long paperAnswerId) {
        Optional<PaperAnswer> paperAnswerOptional = paperAnswerRepository.findById(paperAnswerId);
        if(paperAnswerOptional.isEmpty()) {
            throw new BadParamException(ResultEnum.PAPER_ANSWER_NOT_EXIST);
        }
        PaperAnswer paperAnswer = paperAnswerOptional.get();
        Paper paper = paperAnswer.getPaper();
        if(!paper.getState().equals(PaperStateEnum.ANSWERING.getState()) &&
                !paper.getState().equals(PaperStateEnum.READY_TO_ANSWERING.getState())) {
            throw new NotAllowOperationException(ResultEnum.PAPER_STATE_NOT_ANSWERING);
        }
        // 判断是否为自己的答卷
        if(!paperAnswer.getUser().getId().equals(userId)) {
            throw new UnauthorizedOperationException(userId, ResultEnum.ANSWER_OTHERS_PAPER);
        }
        return EntityConvertToDTOUtil.convertProblem(paperAnswer.getNextProblem());
    }

    @Override
    @Transactional
    public void submitAnswer(Long userId, ProblemAnswerSubmitRequest problemAnswerSubmitRequest) {
        // 针对答卷加锁
        Optional<PaperAnswer> paperAnswerOptional =
                paperAnswerRepository.findByIdForUpdate(problemAnswerSubmitRequest.getPaperAnswerId());
        if(paperAnswerOptional.isEmpty()) {
            throw new BadParamException(ResultEnum.PAPER_ANSWER_NOT_EXIST);
        }
        PaperAnswer paperAnswer = paperAnswerOptional.get();
        // 判断是否为自己的答卷
        if(!paperAnswer.getUser().getId().equals(userId)) {
            throw new UnauthorizedOperationException(userId, ResultEnum.ANSWER_OTHERS_PAPER);
        }
        // 判断此试卷当前是否可以作答
        if(!paperAnswer.getPaper().getState().equals(PaperStateEnum.ANSWERING.getState()) &&
                !paperAnswer.getPaper().getState().equals(PaperStateEnum.READY_TO_ANSWERING.getState())) {
            throw new NotAllowOperationException(ResultEnum.PAPER_STATE_NOT_ANSWERING);
        }
        // 保存作答题目
        ProblemAnswer problemAnswer = new ProblemAnswer();
        problemAnswer.setProblem(paperAnswer.getNextProblem());
        problemAnswer.setPaperAnswer(paperAnswer);
        BeanUtils.copyProperties(problemAnswerSubmitRequest, problemAnswer);
        problemAnswer = problemAnswerRepository.save(problemAnswer);
        // 为答卷设置需要答的下一道题
        Integer sort = paperAnswer.getNextProblem().getSort() + 1;
        List<Problem> problems = problemRepository.findByPaperAndSort(paperAnswer.getPaper(), sort);
        Optional<Problem> nextProblemOptional = problems.stream()
                .filter(problem -> problem.getFatherProblem() == null)
                .findFirst();
        Problem nextProblem = null;
        if(nextProblemOptional.isPresent()) {
            nextProblem = nextProblemOptional.get();
        }
        paperAnswer.setNextProblem(nextProblem);
        // 更新试卷最后作答时间
        paperAnswer.setFinishTime(new Date());
        // 更新试卷状态
        int takeTime = DateUtil.calLastedTime(paperAnswer.getCreatedTime(), paperAnswer.getFinishTime());
        boolean isOverTime = takeTime > paperAnswer.getPaper().getTime() * 60;
        if(isOverTime) {
            paperAnswer.setState(PaperAnswerStateEnum.OVERTIME.getState());
        } else if(nextProblem == null) {
            paperAnswer.setState(PaperAnswerStateEnum.FINISH.getState());
        }
        paperAnswerRepository.save(paperAnswer);
    }
}
