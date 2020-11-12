package com.bjfu.exam.service.impl;

import com.bjfu.exam.dto.answer.PaperAnswerDTO;
import com.bjfu.exam.dto.answer.PaperAnswerDetailDTO;
import com.bjfu.exam.dto.answer.ProblemAnswerDTO;
import com.bjfu.exam.dto.paper.ProblemDTO;
import com.bjfu.exam.entity.answer.PaperAnswer;
import com.bjfu.exam.entity.answer.ProblemAnswer;
import com.bjfu.exam.entity.paper.Paper;
import com.bjfu.exam.entity.paper.PolymerizationProblem;
import com.bjfu.exam.entity.paper.Problem;
import com.bjfu.exam.entity.user.User;
import com.bjfu.exam.enums.PaperAnswerStateEnum;
import com.bjfu.exam.enums.PaperStateEnum;
import com.bjfu.exam.enums.ResponseBodyEnum;
import com.bjfu.exam.exception.*;
import com.bjfu.exam.repository.answer.PaperAnswerRepository;
import com.bjfu.exam.repository.answer.ProblemAnswerRepository;
import com.bjfu.exam.repository.paper.PaperRepository;
import com.bjfu.exam.repository.paper.PolymerizationProblemRepository;
import com.bjfu.exam.repository.paper.ProblemRepository;
import com.bjfu.exam.repository.user.UserRepository;
import com.bjfu.exam.request.answer.PaperAnswerCreateRequest;
import com.bjfu.exam.request.answer.ProblemAnswerSubmitRequest;
import com.bjfu.exam.service.AnswerService;
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
    @Autowired
    private PolymerizationProblemRepository polymerizationProblemRepository;

    @Override
    public List<PaperAnswerDetailDTO> getPaperAnswers(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()) {
            throw new UserNotExistException(userId, ResponseBodyEnum.USER_NOT_EXIST);
        }
        User user = userOptional.get();
        List<PaperAnswer> paperAnswers = paperAnswerRepository.findAllByUser(user);
        List<PaperAnswerDetailDTO> paperAnswerDetailDTOS = paperAnswers.stream()
                .map(EntityConvertToDTOUtil::convertPaperAnswerToDetail)
                .collect(Collectors.toList());
        return paperAnswerDetailDTOS;
    }

    @Override
    public PaperAnswerDetailDTO getPaperAnswerDetail(Long userId, Long paperAnswerId) {
        Optional<PaperAnswer> paperAnswerOptional = paperAnswerRepository.findById(paperAnswerId);
        if(paperAnswerOptional.isEmpty()) {
            throw new BadParamException(ResponseBodyEnum.PAPER_ANSWER_NOT_EXIST);
        }
        PaperAnswer paperAnswer = paperAnswerOptional.get();
        // 非试卷创建者或答卷创建者 不能获取答卷详情
        if(!paperAnswer.getUser().getId().equals(userId) &&
                !paperAnswer.getPaper().getCreator().getId().equals(userId)) {
            throw new UnauthorizedOperationException(userId, ResponseBodyEnum.GET_OTHERS_PAPER_ANSWER);
        }
        PaperAnswerDetailDTO paperAnswerDetailDTO = EntityConvertToDTOUtil.convertPaperAnswerToDetail(paperAnswer);
        return paperAnswerDetailDTO;
    }

    @Override
    @Transactional
    public PaperAnswerDTO createPaperAnswer(Long userId, PaperAnswerCreateRequest paperAnswerCreateRequest) {
        // 针对此用户加锁
        Optional<User> userOptional = userRepository.findByIdForUpdate(userId);
        if(userOptional.isEmpty()) {
            throw new UserNotExistException(userId, ResponseBodyEnum.USER_NOT_EXIST);
        }
        Optional<Paper> paperOptional = paperRepository.findById(paperAnswerCreateRequest.getPaperId());
        if(paperOptional.isEmpty()) {
            throw new BadParamException(ResponseBodyEnum.PAPER_NOT_EXIST);
        }
        Paper paper = paperOptional.get();
        User user = userOptional.get();
        // 判断此试卷当前是否可以作答
        if(!paper.getState().equals(PaperStateEnum.ANSWERING.getState())) {
            throw new NotAllowOperationException(ResponseBodyEnum.PAPER_STATE_NOT_ANSWERING);
        }
        // 判断是否已经作答过此试卷
        if(paperAnswerRepository.findByUserAndPaper(user, paper).isPresent()) {
            throw new NotAllowOperationException(ResponseBodyEnum.ANSWER_TWICE);
        }
        PaperAnswer paperAnswer = new PaperAnswer();
        paperAnswer.setUser(user);
        paperAnswer.setPaper(paper);
        paperAnswer.setNextProblem(findFirstProblem(paper));
        paperAnswer.setState(PaperAnswerStateEnum.ANSWERING.getState());
        BeanUtils.copyProperties(paperAnswerCreateRequest, paperAnswer);
        paperAnswer = paperAnswerRepository.save(paperAnswer);
        PaperAnswerDTO paperAnswerDTO = EntityConvertToDTOUtil.convertPaperAnswer(paperAnswer);
        return paperAnswerDTO;
    }

    @Override
    public ProblemDTO getNextProblem(Long userId, Long paperAnswerId) {
        Optional<PaperAnswer> paperAnswerOptional = paperAnswerRepository.findById(paperAnswerId);
        if(paperAnswerOptional.isEmpty()) {
            throw new BadParamException(ResponseBodyEnum.PAPER_ANSWER_NOT_EXIST);
        }
        PaperAnswer paperAnswer = paperAnswerOptional.get();
        // 判断是否为自己的答卷
        if(!paperAnswer.getUser().getId().equals(userId)) {
            throw new UnauthorizedOperationException(userId, ResponseBodyEnum.ANSWER_OTHERS_PAPER);
        }
        ProblemDTO problemDTO = EntityConvertToDTOUtil.convertProblem(paperAnswer.getNextProblem());
        return problemDTO;
    }

    @Override
    @Transactional
    public ProblemAnswerDTO submitAnswer(Long userId, ProblemAnswerSubmitRequest problemAnswerSubmitRequest) {
        // 针对答卷加锁
        Optional<PaperAnswer> paperAnswerOptional =
                paperAnswerRepository.findByIdForUpdate(problemAnswerSubmitRequest.getPaperAnswerId());
        if(paperAnswerOptional.isEmpty()) {
            throw new BadParamException(ResponseBodyEnum.PAPER_ANSWER_NOT_EXIST);
        }
        PaperAnswer paperAnswer = paperAnswerOptional.get();
        // 判断是否为自己的答卷
        if(!paperAnswer.getUser().getId().equals(userId)) {
            throw new UnauthorizedOperationException(userId, ResponseBodyEnum.ANSWER_OTHERS_PAPER);
        }
        // 获取作答的题目
        Problem problem = paperAnswer.getNextProblem();
        // 保存作答题目
        ProblemAnswer problemAnswer = new ProblemAnswer();
        problemAnswer.setProblem(problem);
        problemAnswer.setPaperAnswer(paperAnswer);
        BeanUtils.copyProperties(problemAnswerSubmitRequest, problemAnswer);
        problemAnswer = problemAnswerRepository.save(problemAnswer);
        // 为答卷设置需要答的下一道题
        Problem nextProblem = findNextProblem(problem);
        paperAnswer.setNextProblem(nextProblem);
        // 判断试卷是否作答完成
        if(nextProblem == null) {
            paperAnswer.setState(PaperAnswerStateEnum.FINISH.getState());
        }
        paperAnswerRepository.save(paperAnswer);
        ProblemAnswerDTO problemAnswerDTO = EntityConvertToDTOUtil.convertProblemAnswer(problemAnswer);
        return problemAnswerDTO;
    }

    /**
     * 根据小题获取下一题
     */
    private Problem findNextProblem(Problem problem) {
        if(problem == null) {
            return null;
        }
        Paper paper = problem.getPaper();
        int nextSort = problem.getSort() + 1;
        PolymerizationProblem polymerizationProblem = problem.getPolymerizationProblem();
        if(polymerizationProblem != null) {
            Optional<Problem> problem2 = polymerizationProblem.getProblems().stream()
                    .filter((problem1 -> problem1.getSort() > problem.getSort()))
                    .min(Comparator.comparingInt(Problem::getSort));
            if(problem2.isPresent()) {
               return problem2.get();
            } else {
                nextSort = polymerizationProblem.getSort() + 1;
            }
        }
        Optional<Problem> problemOptional
                = problemRepository.findByPaperAndPolymerizationProblemAndSort(paper, null, nextSort);
        if(problemOptional.isPresent()) {
            return problemOptional.get();
        }
        Optional<PolymerizationProblem> polymerizationProblemOptional
                = polymerizationProblemRepository.findByPaperAndSort(paper, nextSort);
        if (polymerizationProblemOptional.isPresent()) {
            PolymerizationProblem polymerizationProblem1 = polymerizationProblemOptional.get();
            Optional<Problem> problem1 = polymerizationProblem1.getProblems().stream()
                    .min(Comparator.comparingInt(Problem::getSort));
            if(problem1.isPresent()) {
                return problem1.get();
            }
        }
        return null;
    }
    /**
     * 获取试卷中的第一题
     */
    private Problem findFirstProblem(Paper paper) {
        Optional<Problem> problemOptional =
                problemRepository.findByPaperAndPolymerizationProblemAndSort(paper, null, 1);
        if(problemOptional.isPresent()) {
            return problemOptional.get();
        }
        Optional<PolymerizationProblem> polymerizationProblemOptional =
                polymerizationProblemRepository.findByPaperAndSort(paper, 1);
        if(polymerizationProblemOptional.isPresent()) {
            PolymerizationProblem polymerizationProblem = polymerizationProblemOptional.get();
            Optional<Problem> problemOptional1 = polymerizationProblem.getProblems().stream().min(Comparator.comparingInt(Problem::getSort));
            if(problemOptional1.isPresent() && problemOptional1.get().getSort() == 1) {
                return problemOptional1.get();
            }
        }
        return null;
    }
}
