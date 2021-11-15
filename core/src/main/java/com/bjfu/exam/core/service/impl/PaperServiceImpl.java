package com.bjfu.exam.core.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.bjfu.exam.dao.entity.paper.Paper;
import com.bjfu.exam.api.enums.UserTypeEnum;
import com.bjfu.exam.core.exception.BadParamExceptionExam;
import com.bjfu.exam.core.exception.UnauthorizedOperationExceptionExam;
import com.bjfu.exam.dto.paper.PaperDTO;
import com.bjfu.exam.dto.paper.PaperDetailDTO;
import com.bjfu.exam.dto.paper.PaperWithProblemsDTO;
import com.bjfu.exam.dto.paper.ProblemDTO;
import com.bjfu.exam.dao.entity.paper.Problem;
import com.bjfu.exam.dao.entity.user.User;
import com.bjfu.exam.api.enums.PaperStateEnum;
import com.bjfu.exam.api.enums.ProblemTypeEnum;
import com.bjfu.exam.api.enums.ResultEnum;
import com.bjfu.exam.core.exception.NotAllowOperationExceptionExam;
import com.bjfu.exam.core.exception.UserNotExistExceptionExam;
import com.bjfu.exam.dao.repository.ImgFileRepository;
import com.bjfu.exam.dao.repository.paper.PaperRepository;
import com.bjfu.exam.dao.repository.paper.ProblemRepository;
import com.bjfu.exam.dao.repository.user.UserRepository;
import com.bjfu.exam.request.paper.*;
import com.bjfu.exam.service.PaperService;
import exam.request.paper.*;
import com.bjfu.exam.core.util.EntityConvertToDTOUtil;
import com.bjfu.exam.core.util.RandomCodeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaperServiceImpl implements PaperService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PaperRepository paperRepository;
    @Autowired
    ProblemRepository problemRepository;
    @Autowired
    ImgFileRepository imgFileRepository;

    @Override
    public PaperDetailDTO getPaperByCode(String code) {
        Optional<Paper> paperOptional = paperRepository.findByCode(code);
        if(paperOptional.isEmpty() ||
                paperOptional.get().getState().equals(PaperStateEnum.CREATING.getState()) ||
                paperOptional.get().getState().equals(PaperStateEnum.SOFT_DELETE.getState())) {
            return null;
        }
        return EntityConvertToDTOUtil.convertPaperToDetails(paperOptional.get());
    }

    @Override
    public PaperWithProblemsDTO getPaperDetail(Long paperId , Long userId) {
        Optional<Paper> paperOptional = paperRepository.findById(paperId);
        if(paperOptional.isEmpty()) {
            return null;
        }
        Paper paper = paperOptional.get();
        if(!paper.getCreator().getId().equals(userId)) {
            throw new UnauthorizedOperationExceptionExam(userId, ResultEnum.NOT_PAPER_CREATOR);
        }
        return EntityConvertToDTOUtil.convertPaperToWithProblems(paperOptional.get());
    }

    @Override
    public List<PaperDTO> getAllPaperByCreatorId(Long creatorId) {
        Optional<User> userOptional = userRepository.findById(creatorId);
        if(userOptional.isEmpty()) {
            throw new UserNotExistExceptionExam(creatorId, ResultEnum.USER_NOT_EXIST);
        }
        List<Paper> papers = paperRepository.findAllByCreator(userOptional.get());
        return papers.stream()
                .map(EntityConvertToDTOUtil::convertPaper)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PaperDetailDTO createPaper(PaperCreatePaperRequest paperCreatePaperRequest, Long creatorId) {
        Optional<User> userOptional = userRepository.findById(creatorId);
        if(userOptional.isEmpty()) {
            throw new UserNotExistExceptionExam(creatorId, ResultEnum.USER_NOT_EXIST);
        }
        User creator = userOptional.get();
        if(!creator.getType().equals(UserTypeEnum.TEACHER.getType())) {
            throw new UnauthorizedOperationExceptionExam(creatorId, ResultEnum.NOT_TEACHER_CREATE_PAPER);
        }
        Paper paper = new Paper();
        BeanUtils.copyProperties(paperCreatePaperRequest, paper);
        paper.setCreator(creator);
        String code = RandomCodeUtil.nextCodeWithCharAndNumber();
        while(paperRepository.existsByCode(code)) {
            code = RandomCodeUtil.nextCodeWithCharAndNumber();
        }
        paper.setCode(code);
        paper.setState(PaperStateEnum.CREATING.getState());
        paper = paperRepository.save(paper);
        return EntityConvertToDTOUtil.convertPaperToDetails(paper);
    }

    @Override
    public PaperDetailDTO editPaper(PaperEditPaperRequest paperEditPaperRequest, Long creatorId) {
        //查找试卷是否存在
        Optional<Paper> paperOptional = paperRepository.findByIdForUpdate(paperEditPaperRequest.getPaperId());
        if(paperOptional.isEmpty() ||
                paperOptional.get().getState().equals(PaperStateEnum.SOFT_DELETE.getState())) {
            throw new BadParamExceptionExam(ResultEnum.PAPER_NOT_EXIST);
        }
        //查找账号是否存在
        Paper paper = paperOptional.get();
        Optional<User> userOptional = userRepository.findById(creatorId);
        if(userOptional.isEmpty()) {
            throw new UserNotExistExceptionExam(creatorId, ResultEnum.USER_NOT_EXIST);
        }
        //判断是否为试卷创建者
        User creator = userOptional.get();
        if(!paper.getCreator().getId().equals(creator.getId())) {
            throw new UnauthorizedOperationExceptionExam(creatorId, ResultEnum.NOT_PAPER_CREATOR);
        }
        //修改并保存
        paper.setTitle(paperEditPaperRequest.getTitle());
        paper.setIntroduction(paperEditPaperRequest.getIntroduction());
        paper = paperRepository.save(paper);
        return EntityConvertToDTOUtil.convertPaperToDetails(paper);
    }

    @Override
    @Transactional
    public ProblemDTO addProblem(Long userId, ProblemAddRequest problemAddRequest) {
        // 为此试卷加锁
        Optional<Paper> paperOptional = paperRepository.findByIdForUpdate(problemAddRequest.getPaperId());
        if(paperOptional.isEmpty() ||
                paperOptional.get().getState().equals(PaperStateEnum.SOFT_DELETE.getState())) {
            throw new BadParamExceptionExam(ResultEnum.PAPER_NOT_EXIST);
        }
        Paper paper = paperOptional.get();
        if(!paper.getCreator().getId().equals(userId)) {
            throw new UnauthorizedOperationExceptionExam(userId, ResultEnum.NOT_PAPER_CREATOR);
        }
        if(!paper.getState().equals(PaperStateEnum.CREATING.getState())) {
            throw new NotAllowOperationExceptionExam(ResultEnum.PAPER_STATE_IS_NOT_CREATING);
        }
        List<Problem> bigProblems = paper.getProblems().stream()
                .filter(problem -> problem.getFatherProblem() == null)
                .collect(Collectors.toList());
        Long paperId = problemAddRequest.getPaperId();
        Long fatherProblemId = problemAddRequest.getFatherProblemId();
        Problem problem = new Problem();
        int sort = 1;
        if(paperId != null && fatherProblemId == null) {
            // 为试卷添加大题
            if(!bigProblems.isEmpty()) {
                sort = bigProblems.get(bigProblems.size() - 1).getSort() + 1;
            }
        } else if(paperId != null){
            // 为试卷中的组合题目添加小题
            Optional<Problem> fatherProblemOptional = problemRepository.findById(fatherProblemId);
            if(fatherProblemOptional.isEmpty()) {
                throw new BadParamExceptionExam(ResultEnum.PROBLEM_NOT_EXIST);
            }
            Problem fatherProblem = fatherProblemOptional.get();
            if(!fatherProblem.getType().equals(ProblemTypeEnum.FATHER_PROBLEM.getType())) {
                throw new BadParamExceptionExam(ResultEnum.PARAM_WRONG);
            }
            List<Problem> subProblems = fatherProblem.getSubProblems();
            if(!subProblems.isEmpty()) {
                sort = subProblems.get(subProblems.size() - 1).getSort() + 1;
            }
            problem.setFatherProblem(fatherProblem);
        } else {
            throw new BadParamExceptionExam(ResultEnum.PARAM_WRONG);
        }
        problem.setPaper(paper);
        problem.setSort(sort);
        problem.setImages(new JSONArray().toJSONString());
        BeanUtils.copyProperties(problemAddRequest, problem);
        problem = problemRepository.save(problem);
        return EntityConvertToDTOUtil.convertProblem(problem);
    }

    @Override
    public PaperDetailDTO editProblem(ProblemEditRequest problemEditRequest, Long creatorId) {
        //查找试题是否存在
        Optional<Problem> problemOptional = problemRepository.findByIdForUpdate(problemEditRequest.getProblemId());
        if(problemOptional.isEmpty() ||
                problemOptional.get().getPaper().getState().equals(PaperStateEnum.SOFT_DELETE.getState())) {
            throw new BadParamExceptionExam(ResultEnum.PROBLEM_NOT_EXIST);
        }
        //查找账号是否存在
        Problem problem = problemOptional.get();
        Paper paper = problem.getPaper();
        Optional<User> userOptional = userRepository.findById(creatorId);
        if(userOptional.isEmpty()) {
            throw new UserNotExistExceptionExam(creatorId, ResultEnum.USER_NOT_EXIST);
        }
        //判断是否为试卷创建者
        User creator = userOptional.get();
        if(!paper.getCreator().getId().equals(creator.getId())) {
            throw new UnauthorizedOperationExceptionExam(creatorId, ResultEnum.NOT_PAPER_CREATOR);
        }
        //修改并保存
        problem.setTitle(problemEditRequest.getTitle());
        problem.setMaterial(problemEditRequest.getMaterial());
        if(problem.getType().equals(ProblemTypeEnum.CHOICE_PROBLEM.getType())) {
            problem.setAnswer(problemEditRequest.getAnswer());
        }
        problemRepository.save(problem);
        return EntityConvertToDTOUtil.convertPaperToDetails(paper);
    }

    @Override
    @Transactional
    public ProblemDTO addImageInProblem(Long userId, ImageInProblemAddRequest imageInProblemAddRequest) throws IOException {
        // 为此问题加锁
        Optional<Problem> problemOptional = problemRepository.findByIdForUpdate(imageInProblemAddRequest.getProblemId());
        if(problemOptional.isEmpty() ||
                problemOptional.get().getPaper().getState().equals(PaperStateEnum.SOFT_DELETE.getState())) {
            throw new BadParamExceptionExam(ResultEnum.PROBLEM_NOT_EXIST);
        }
        Problem problem = problemOptional.get();
        if(!problem.getPaper().getCreator().getId().equals(userId)) {
            throw new UnauthorizedOperationExceptionExam(userId, ResultEnum.NOT_PAPER_CREATOR);
        }
        if(!problem.getPaper().getState().equals(PaperStateEnum.CREATING.getState())) {
            throw new NotAllowOperationExceptionExam(ResultEnum.PAPER_STATE_IS_NOT_CREATING);
        }
        String imgName = UUID.randomUUID().toString();
        String images = problem.getImages();
        if(StringUtils.isEmpty(images)) {
            images = new JSONArray().toJSONString();
        }
        JSONArray jsonArray = JSONArray.parseArray(images);
        jsonArray.add(imageInProblemAddRequest.getIndex(), imgName);
        problem.setImages(jsonArray.toJSONString());
        problem = problemRepository.save(problem);
        imgFileRepository.uploadFile(imgName, imageInProblemAddRequest.getImgFile().getInputStream());
        return EntityConvertToDTOUtil.convertProblem(problem);
    }

    @Override
    @Transactional
    public ProblemDTO deleteImageInProblem(Long userId, ImageInProblemDeleteRequest imageInProblemDeleteRequest) {
        // 为此问题加锁
        Optional<Problem> problemOptional = problemRepository.findByIdForUpdate(imageInProblemDeleteRequest.getProblemId());
        if(problemOptional.isEmpty() ||
                problemOptional.get().getPaper().getState().equals(PaperStateEnum.SOFT_DELETE.getState())) {
            throw new BadParamExceptionExam(ResultEnum.PROBLEM_NOT_EXIST);
        }
        Problem problem = problemOptional.get();
        if(!problem.getPaper().getCreator().getId().equals(userId)) {
            throw new UnauthorizedOperationExceptionExam(userId, ResultEnum.NOT_PAPER_CREATOR);
        }
        if(!problem.getPaper().getState().equals(PaperStateEnum.CREATING.getState())) {
            throw new NotAllowOperationExceptionExam(ResultEnum.PAPER_STATE_IS_NOT_CREATING);
        }
        String images = problem.getImages();
        if(StringUtils.isEmpty(images)) {
            images = new JSONArray().toJSONString();
        }
        JSONArray jsonArray = JSONArray.parseArray(images);
        int removeIndex = imageInProblemDeleteRequest.getIndex();
        if(removeIndex < 0 || removeIndex >= jsonArray.size()) {
            throw new BadParamExceptionExam(ResultEnum.PARAM_WRONG);
        }
        String removedImage = (String) jsonArray.remove(removeIndex);
        problem.setImages(jsonArray.toJSONString());
        problem = problemRepository.save(problem);
        imgFileRepository.deleteFile(removedImage);
        return EntityConvertToDTOUtil.convertProblem(problem);
    }

    @Override
    @Transactional
    public void deleteProblem(Long userId, ProblemDeleteRequest problemDeleteRequest) {
        // 为此试卷加锁
        Optional<Paper> paperOptional = paperRepository.findByIdForUpdate(problemDeleteRequest.getPaperId());
        if(paperOptional.isEmpty() ||
                paperOptional.get().getState().equals(PaperStateEnum.SOFT_DELETE.getState())) {
            throw new BadParamExceptionExam(ResultEnum.PAPER_NOT_EXIST);
        }
        Paper paper = paperOptional.get();
        if(!paper.getCreator().getId().equals(userId)) {
            throw new UnauthorizedOperationExceptionExam(userId, ResultEnum.NOT_PAPER_CREATOR);
        }
        if(!paper.getState().equals(PaperStateEnum.CREATING.getState())) {
            throw new NotAllowOperationExceptionExam(ResultEnum.PAPER_STATE_IS_NOT_CREATING);
        }
        Optional<Problem> problemOptional = problemRepository.findById(problemDeleteRequest.getProblemId());
        if(problemOptional.isEmpty()) {
            throw new BadParamExceptionExam(ResultEnum.PROBLEM_NOT_EXIST);
        }
        Problem problem = problemOptional.get();
        if(problem.getPaper() == null || !problem.getPaper().getId().equals(paper.getId())) {
            //问题和试卷不相等
            throw new BadParamExceptionExam(ResultEnum.PARAM_WRONG);
        }
        List<String> deleteImages = new ArrayList<>();
        Problem fatherProblem = problem.getFatherProblem();
        if(fatherProblem == null) {
            // 删除大题
            List<Problem> bigProblems = paper.getProblems().stream()
                    .filter(problem1 -> problem1.getFatherProblem() == null)
                    .collect(Collectors.toList());
            if(problem.getType().equals(ProblemTypeEnum.FATHER_PROBLEM.getType())) {
                // 删除复合题目中的小题
                List<Problem> subProblems = problem.getSubProblems();
                subProblems.forEach(subProblem -> {
                    String images = subProblem.getImages();
                    if(StringUtils.isEmpty(images)) {
                        images = new JSONArray().toJSONString();
                    }
                    deleteImages.addAll(JSONArray.parseArray(images).toJavaList(String.class));
                });
                problemRepository.deleteAll(problem.getSubProblems());
            }
            bigProblems.forEach(problem1 -> {
                if(problem1.getSort() > problem.getSort()) {
                    problem1.setSort(problem1.getSort() - 1);
                }
            });
            problemRepository.saveAll(bigProblems);
        } else {
            // 删除子题目
            List<Problem> subProblems = fatherProblem.getSubProblems();
            subProblems.forEach(problem1 -> {
                if(problem1.getSort() > problem.getSort()) {
                    problem1.setSort(problem1.getSort() - 1);
                }
            });
            problemRepository.saveAll(subProblems);
        }
        String images = problem.getImages();
        if(StringUtils.isEmpty(images)) {
            images = new JSONArray().toJSONString();
        }
        deleteImages.addAll(JSONArray.parseArray(images).toJavaList(String.class));
        problemRepository.delete(problem);
        imgFileRepository.deleteFiles(deleteImages);
    }

    @Override
    @Transactional
    public void deletePaper(Long userId, Long paperId) {
        // 为此试卷加锁
        Optional<Paper> paperOptional = paperRepository.findByIdForUpdate(paperId);
        if(paperOptional.isEmpty() ||
                paperOptional.get().getState().equals(PaperStateEnum.SOFT_DELETE.getState())) {
            throw new BadParamExceptionExam(ResultEnum.PAPER_NOT_EXIST);
        }
        Paper paper = paperOptional.get();
        if(!paper.getCreator().getId().equals(userId)) {
            throw new UnauthorizedOperationExceptionExam(userId, ResultEnum.NOT_PAPER_CREATOR);
        }
        Integer state = paper.getState();
        if(!state.equals(PaperStateEnum.CREATING.getState()) &&
                !state.equals(PaperStateEnum.END_ANSWER.getState())) {
            throw new NotAllowOperationExceptionExam(ResultEnum.PAPER_STATE_CAN_NOT_DELETE);
        }
        paper.setCode(null);
        paper.setState(PaperStateEnum.SOFT_DELETE.getState());
        paperRepository.save(paper);
    }

    private final Set<String> stateChange = new HashSet<>();
    {
        stateChange.add(PaperStateEnum.CREATING.getState()+"-"+PaperStateEnum.READY_TO_ANSWERING.getState());
        stateChange.add(PaperStateEnum.READY_TO_ANSWERING.getState()+"-"+PaperStateEnum.CREATING.getState());
        stateChange.add(PaperStateEnum.ANSWERING.getState()+"-"+PaperStateEnum.END_ANSWER.getState());
        stateChange.add(PaperStateEnum.END_ANSWER.getState()+"-"+PaperStateEnum.ANSWERING.getState());
    }

    @Override
    public PaperDTO changePaperState(Long userId, PaperChangePaperStateRequest paperChangePaperStateRequest) {
        // 为此试卷加锁
        Optional<Paper> paperOptional = paperRepository.findByIdForUpdate(paperChangePaperStateRequest.getPaperId());
        if(paperOptional.isEmpty() ||
                paperOptional.get().getState().equals(PaperStateEnum.SOFT_DELETE.getState())) {
            throw new BadParamExceptionExam(ResultEnum.PAPER_NOT_EXIST);
        }
        Paper paper = paperOptional.get();
        if(!paper.getCreator().getId().equals(userId)) {
            throw new UnauthorizedOperationExceptionExam(userId, ResultEnum.NOT_PAPER_CREATOR);
        }
        if(!stateChange.contains(paper.getState()+"-"+ paperChangePaperStateRequest.getState())) {
            throw new NotAllowOperationExceptionExam(ResultEnum.PAPER_STATE_CHANGE_NOT_ALLOW);
        }
        paper.setState(paperChangePaperStateRequest.getState());
        paper = paperRepository.save(paper);
        return EntityConvertToDTOUtil.convertPaper(paper);
    }

}
