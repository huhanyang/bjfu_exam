package com.bjfu.exam.service.impl;

import com.bjfu.exam.dto.answer.PaperAnswerDTO;
import com.bjfu.exam.dto.answer.PaperAnswerDetailDTO;
import com.bjfu.exam.dto.answer.ProblemAnswerDTO;
import com.bjfu.exam.dto.paper.PolymerizationProblemDTO;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
    public List<PaperAnswerDTO> getPaperAnswers(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()) {
            throw new UserNotExistException(userId, ResponseBodyEnum.USER_NOT_EXIST);
        }
        User user = userOptional.get();
        List<PaperAnswer> paperAnswers = paperAnswerRepository.findAllByUser(user);
        List<PaperAnswerDTO> paperAnswerDTOS = paperAnswers.stream()
                .map(EntityConvertToDTOUtil::convertPaperAnswer)
                .collect(Collectors.toList());
        return paperAnswerDTOS;
    }

    @Override
    public PaperAnswerDetailDTO getPaperAnswerDetail(Long userId, Long paperAnswerId) {
        Optional<PaperAnswer> paperAnswerOptional = paperAnswerRepository.findById(paperAnswerId);
        if(paperAnswerOptional.isEmpty()) {
            throw new BadParamException(ResponseBodyEnum.PAPER_ANSWER_NOT_EXIST);
        }
        PaperAnswer paperAnswer = paperAnswerOptional.get();
        // 非试卷创建者或答卷创建者 不能获取答卷详情
        if(!paperAnswer.getUser().getId().equals(userId) ||
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
        paperAnswer.setState(PaperAnswerStateEnum.ANSWERING.getState());
        BeanUtils.copyProperties(paperAnswerCreateRequest, paperAnswer);
        paperAnswer = paperAnswerRepository.save(paperAnswer);
        PaperAnswerDTO paperAnswerDTO = EntityConvertToDTOUtil.convertPaperAnswer(paperAnswer);
        return paperAnswerDTO;
    }

    @Override
    public ProblemDTO getProblem(Long userId, Long paperAnswerId) {
        Optional<PaperAnswer> paperAnswerOptional = paperAnswerRepository.findById(paperAnswerId);
        if(paperAnswerOptional.isEmpty()) {
            throw new BadParamException(ResponseBodyEnum.PAPER_ANSWER_NOT_EXIST);
        }
        PaperAnswer paperAnswer = paperAnswerOptional.get();
        // 判断是否为自己的答卷
        if(!paperAnswer.getUser().getId().equals(userId)) {
            throw new UnauthorizedOperationException(userId, ResponseBodyEnum.ANSWER_OTHERS_PAPER);
        }
        Paper paper = paperAnswer.getPaper();
        // 判断当前试卷可以继续作答
        if(!paper.getState().equals(PaperStateEnum.ANSWERING.getState())) {
            throw new NotAllowOperationException(ResponseBodyEnum.PAPER_STATE_NOT_ANSWERING);
        }
        // 根据题号获取试题
        int size = paperAnswer.getProblemAnswers().size();
        Optional<Problem> problemOptional = problemRepository.findByPaperAndSort(paper, size + 1);
        if(problemOptional.isEmpty()) {
            return null;
        }
        Problem problem = problemOptional.get();
        ProblemDTO problemDTO = EntityConvertToDTOUtil.convertProblem(problem);
        return problemDTO;
    }

    @Override
    public PolymerizationProblemDTO getPolymerizationProblem(Long userId, Long paperAnswerId) {
        Optional<PaperAnswer> paperAnswerOptional = paperAnswerRepository.findById(paperAnswerId);
        if(paperAnswerOptional.isEmpty()) {
            throw new BadParamException(ResponseBodyEnum.PAPER_ANSWER_NOT_EXIST);
        }
        PaperAnswer paperAnswer = paperAnswerOptional.get();
        // 判断是否为自己的答卷
        if(!paperAnswer.getUser().getId().equals(userId)) {
            throw new UnauthorizedOperationException(userId, ResponseBodyEnum.ANSWER_OTHERS_PAPER);
        }
        Paper paper = paperAnswer.getPaper();
        // 判断当前试卷可以继续作答
        if(!paper.getState().equals(PaperStateEnum.ANSWERING.getState())) {
            throw new NotAllowOperationException(ResponseBodyEnum.PAPER_STATE_NOT_ANSWERING);
        }
        // 根据题号获取试题
        int size = paperAnswer.getProblemAnswers().size();
        Optional<PolymerizationProblem> polymerizationProblemOptional =
                polymerizationProblemRepository.findByPaperAndSort(paper, size + 1);
        if(polymerizationProblemOptional.isEmpty()) {
            return null;
        }
        PolymerizationProblem polymerizationProblem = polymerizationProblemOptional.get();
        PolymerizationProblemDTO polymerizationProblemDTO =
                EntityConvertToDTOUtil.convertPolymerizationProblem(polymerizationProblem);
        return polymerizationProblemDTO;
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
        Paper paper = paperAnswer.getPaper();
        // 判断当前试卷可以继续作答
        if(!paper.getState().equals(PaperStateEnum.ANSWERING.getState())) {
            throw new NotAllowOperationException(ResponseBodyEnum.PAPER_STATE_NOT_ANSWERING);
        }
        Optional<Problem> problemOptional = problemRepository.findById(problemAnswerSubmitRequest.getProblemId());
        if(problemOptional.isEmpty()) {
            throw new BadParamException(ResponseBodyEnum.PARAM_WRONG);
        }
        Problem problem = problemOptional.get();
        // 判断试题与试卷是否匹配
        if(!problem.getPaper().getId().equals(paper.getId())) {
            throw new BadParamException(ResponseBodyEnum.PARAM_WRONG);
        }
        PolymerizationProblem polymerizationProblem = problem.getPolymerizationProblem();
        ProblemAnswer problemAnswer = new ProblemAnswer();
        problemAnswer.setProblem(problem);
        problemAnswer.setPaperAnswer(paperAnswer);
        Set<ProblemAnswer> problemAnswers = paperAnswer.getProblemAnswers();
        // 根据是否为组合题目的小题来计算是否为连续答题
        if(polymerizationProblem == null) {
            int size = problemAnswers.size();
            if(size != problem.getSort() - 1) {
                throw new NotAllowOperationException(ResponseBodyEnum.NOT_CONTINUOUS_ANSWERING);
            }
        } else {
            Map<Long, Integer> map = polymerizationProblem.getProblems().stream()
                    .collect(Collectors.toMap(Problem::getId, Problem::getSort));
            int size = (int) problemAnswers.stream()
                    .filter(problemAnswer1 -> map.containsKey(problemAnswer1.getProblem().getId()))
                    .count();
            if(size != problem.getSort() - 1) {
                throw new NotAllowOperationException(ResponseBodyEnum.NOT_CONTINUOUS_ANSWERING);
            }
        }
        BeanUtils.copyProperties(problemAnswerSubmitRequest, problemAnswer);
        problemAnswer = problemAnswerRepository.save(problemAnswer);
        // 判断试卷是否作答完成
        int answerProblemSize = paperAnswer.getProblemAnswers().size() + 1;
        int problemSize = paper.getProblems().size();
        if(answerProblemSize == problemSize) {
            paperAnswer.setState(PaperAnswerStateEnum.FINISH.getState());
            paperAnswerRepository.save(paperAnswer);
        }
        ProblemAnswerDTO problemAnswerDTO = EntityConvertToDTOUtil.convertProblemAnswer(problemAnswer);
        return problemAnswerDTO;
    }
}
