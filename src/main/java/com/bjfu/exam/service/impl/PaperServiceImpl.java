package com.bjfu.exam.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjfu.exam.dto.paper.PaperDTO;
import com.bjfu.exam.dto.paper.PaperDetailDTO;
import com.bjfu.exam.dto.paper.PolymerizationProblemDetailDTO;
import com.bjfu.exam.dto.paper.ProblemDTO;
import com.bjfu.exam.entity.answer.ProblemAnswer;
import com.bjfu.exam.entity.paper.Paper;
import com.bjfu.exam.entity.paper.PolymerizationProblem;
import com.bjfu.exam.entity.paper.Problem;
import com.bjfu.exam.entity.user.User;
import com.bjfu.exam.enums.PaperStateEnum;
import com.bjfu.exam.enums.ResultEnum;
import com.bjfu.exam.enums.UserTypeEnum;
import com.bjfu.exam.exception.*;
import com.bjfu.exam.repository.ImgFileRepository;
import com.bjfu.exam.repository.answer.PaperAnswerRepository;
import com.bjfu.exam.repository.answer.ProblemAnswerRepository;
import com.bjfu.exam.repository.paper.PaperRepository;
import com.bjfu.exam.repository.paper.PolymerizationProblemRepository;
import com.bjfu.exam.repository.paper.ProblemRepository;
import com.bjfu.exam.repository.user.UserRepository;
import com.bjfu.exam.request.paper.*;
import com.bjfu.exam.service.PaperService;
import com.bjfu.exam.util.EntityConvertToDTOUtil;
import com.bjfu.exam.util.RandomCodeUtil;
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
    PolymerizationProblemRepository polymerizationProblemRepository;
    @Autowired
    ImgFileRepository imgFileRepository;

    @Override
    public PaperDTO getPaper(String code) {
        Optional<Paper> paperOptional = paperRepository.findByCode(code);
        if(paperOptional.isEmpty()) {
            return null;
        }
        return EntityConvertToDTOUtil.convertPaper(paperOptional.get());
    }

    @Override
    public PaperDetailDTO getPaperDetail(Long paperId ,Long userId) {
        Optional<Paper> paperOptional = paperRepository.findById(paperId);
        if(paperOptional.isEmpty()) {
            return null;
        }
        Paper paper = paperOptional.get();
        if(!paper.getCreator().getId().equals(userId)) {
            throw new UnauthorizedOperationException(userId, ResultEnum.NOT_CREATOR_EDIT_PAPER);
        }
        return EntityConvertToDTOUtil.convertPaperToDetail(paperOptional.get());
    }

    @Override
    public List<PaperDetailDTO> getAllPaperByCreatorId(Long creatorId) {
        Optional<User> userOptional = userRepository.findById(creatorId);
        if(userOptional.isEmpty()) {
            throw new UserNotExistException(creatorId, ResultEnum.USER_NOT_EXIST);
        }
        List<Paper> papers = paperRepository.findAllByCreator(userOptional.get());
        return papers.stream()
                .map(EntityConvertToDTOUtil::convertPaperToDetail)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PaperDetailDTO createPaper(PaperCreateRequest paperCreateRequest, Long creatorId) {
        Optional<User> userOptional = userRepository.findById(creatorId);
        if(userOptional.isEmpty()) {
            throw new UserNotExistException(creatorId, ResultEnum.USER_NOT_EXIST);
        }
        User creator = userOptional.get();
        if(!creator.getType().equals(UserTypeEnum.TEACHER.getType())) {
            throw new UnauthorizedOperationException(creatorId, ResultEnum.NOT_TEACHER_CREATE_PAPER);
        }
        Paper paper = new Paper();
        BeanUtils.copyProperties(paperCreateRequest, paper);
        paper.setCreator(creator);
        String code = RandomCodeUtil.nextCodeWithCharAndNumber();
        while(paperRepository.existsByCode(code)) {
            code = RandomCodeUtil.nextCodeWithCharAndNumber();
        }
        paper.setCode(code);
        paper.setState(PaperStateEnum.CREATING.getState());
        paper = paperRepository.save(paper);
        return EntityConvertToDTOUtil.convertPaperToDetail(paper);
    }

    @Override
    @Transactional
    public PolymerizationProblemDetailDTO addPolymerizationProblemInPaper(Long userId,
                                                                          PolymerizationProblemAddRequest polymerizationProblemAddRequest) {
        // 为此试卷加锁
        paperRepository.existsById(polymerizationProblemAddRequest.getPaperId());
        Optional<Paper> paperOptional = paperRepository.findById(polymerizationProblemAddRequest.getPaperId());
        if(paperOptional.isEmpty()) {
            throw new BadParamException(ResultEnum.PAPER_NOT_EXIST);
        }
        Paper paper = paperOptional.get();
        if(!paper.getCreator().getId().equals(userId)) {
            throw new UnauthorizedOperationException(userId, ResultEnum.NOT_CREATOR_EDIT_PAPER);
        }
        if(!paper.getState().equals(PaperStateEnum.CREATING.getState())) {
            throw new NotAllowOperationException(ResultEnum.PAPER_STATE_IS_NOT_CREATING);
        }
        int sort = paperRepository.getProblemSize(polymerizationProblemAddRequest.getPaperId()) +
                paperRepository.getPolymerizationProblemSize(polymerizationProblemAddRequest.getPaperId()) + 1;
        PolymerizationProblem polymerizationProblem = new PolymerizationProblem();
        BeanUtils.copyProperties(polymerizationProblemAddRequest, polymerizationProblem);
        polymerizationProblem.setSort(sort);
        polymerizationProblem.setPaper(paper);
        polymerizationProblem = polymerizationProblemRepository.save(polymerizationProblem);
        return EntityConvertToDTOUtil.convertPolymerizationProblemDetail(polymerizationProblem);
    }

    @Override
    @Transactional
    public PolymerizationProblemDetailDTO addImageInPolymerizationProblem(Long userId,
                                                                          ImageInPolymerizationProblemAddRequest imageInPolymerizationProblemAddRequest) throws IOException {
        // 对此组合题目加锁
        Optional<PolymerizationProblem> polymerizationProblemOptional =
                polymerizationProblemRepository.findByIdForUpdate(imageInPolymerizationProblemAddRequest.getPolymerizationProblemId());
        if(polymerizationProblemOptional.isEmpty()) {
            throw new BadParamException(ResultEnum.POLYMERIZATION_PROBLEM_NOT_EXIST);
        }
        PolymerizationProblem polymerizationProblem = polymerizationProblemOptional.get();
        if(!polymerizationProblem.getPaper().getCreator().getId().equals(userId)) {
            throw new UnauthorizedOperationException(userId, ResultEnum.NOT_CREATOR_EDIT_PAPER);
        }
        if(!polymerizationProblem.getPaper().getState().equals(PaperStateEnum.CREATING.getState())) {
            throw new NotAllowOperationException(ResultEnum.PAPER_STATE_IS_NOT_CREATING);
        }
        String images = polymerizationProblem.getImages();
        if(StringUtils.isEmpty(images)) {
            images = new JSONArray().toJSONString();
        }
        JSONArray jsonArray = JSONArray.parseArray(images);
        String imgName = UUID.randomUUID().toString();
        jsonArray.add(imageInPolymerizationProblemAddRequest.getIndex() - 1, imgName);
        polymerizationProblem.setImages(jsonArray.toJSONString());
        polymerizationProblem = polymerizationProblemRepository.save(polymerizationProblem);
        imgFileRepository.uploadFile(imgName, imageInPolymerizationProblemAddRequest.getImgFile().getInputStream());
        return EntityConvertToDTOUtil.convertPolymerizationProblemDetail(polymerizationProblem);
    }

    @Override
    @Transactional
    public PolymerizationProblemDetailDTO deleteImageInPolymerizationProblem(Long userId, ImageInPolymerizationProblemDeleteRequest imageInPolymerizationProblemDeleteRequest) {
        // 对此组合题目加锁
        Optional<PolymerizationProblem> polymerizationProblemOptional =
                polymerizationProblemRepository.findByIdForUpdate(imageInPolymerizationProblemDeleteRequest.getPolymerizationProblemId());
        if(polymerizationProblemOptional.isEmpty()) {
            throw new BadParamException(ResultEnum.POLYMERIZATION_PROBLEM_NOT_EXIST);
        }
        PolymerizationProblem polymerizationProblem = polymerizationProblemOptional.get();
        if(!polymerizationProblem.getPaper().getCreator().getId().equals(userId)) {
            throw new UnauthorizedOperationException(userId, ResultEnum.NOT_CREATOR_EDIT_PAPER);
        }
        if(!polymerizationProblem.getPaper().getState().equals(PaperStateEnum.CREATING.getState())) {
            throw new NotAllowOperationException(ResultEnum.PAPER_STATE_IS_NOT_CREATING);
        }
        String images = polymerizationProblem.getImages();
        if(StringUtils.isEmpty(images)) {
            images = new JSONArray().toJSONString();
        }
        JSONArray jsonArray = JSONArray.parseArray(images);
        String remove = (String) jsonArray.remove(imageInPolymerizationProblemDeleteRequest.getIndex() - 1);
        polymerizationProblem.setImages(jsonArray.toJSONString());
        polymerizationProblem = polymerizationProblemRepository.save(polymerizationProblem);
        imgFileRepository.deleteFile(remove);
        return EntityConvertToDTOUtil.convertPolymerizationProblemDetail(polymerizationProblem);
    }

    @Override
    @Transactional
    public ProblemDTO addProblem(Long userId, ProblemAddRequest problemAddRequest) {
        // 为此试卷加锁
        Optional<Paper> paperOptional = paperRepository.findByIdForUpdate(problemAddRequest.getPaperId());
        if(paperOptional.isEmpty()) {
            throw new BadParamException(ResultEnum.PAPER_NOT_EXIST);
        }
        Paper paper = paperOptional.get();
        if(!paper.getCreator().getId().equals(userId)) {
            throw new UnauthorizedOperationException(userId, ResultEnum.NOT_CREATOR_EDIT_PAPER);
        }
        if(!paper.getState().equals(PaperStateEnum.CREATING.getState())) {
            throw new NotAllowOperationException(ResultEnum.PAPER_STATE_IS_NOT_CREATING);
        }
        Problem problem = new Problem();
        if(problemAddRequest.getPaperId() != null && problemAddRequest.getPolymerizationProblemId() == null) {
            int sort = paperRepository.getProblemSize(problemAddRequest.getPaperId()) +
                    paperRepository.getPolymerizationProblemSize(problemAddRequest.getPaperId()) + 1;
            BeanUtils.copyProperties(problemAddRequest, problem);
            problem.setSort(sort);
            problem.setPaper(paper);
            problem = problemRepository.save(problem);
            return EntityConvertToDTOUtil.convertProblem(problem);
        } else if(problemAddRequest.getPaperId() != null && problemAddRequest.getPolymerizationProblemId() != null){
            Optional<PolymerizationProblem> polymerizationProblemOptional = polymerizationProblemRepository
                    .findById(problemAddRequest.getPolymerizationProblemId());
            if(polymerizationProblemOptional.isEmpty()) {
                throw new BadParamException(ResultEnum.POLYMERIZATION_PROBLEM_NOT_EXIST);
            }
            PolymerizationProblem polymerizationProblem = polymerizationProblemOptional.get();
            int sort = polymerizationProblem.getProblems().size() + 1;
            BeanUtils.copyProperties(problemAddRequest, problem);
            problem.setSort(sort);
            problem.setPaper(paper);
            problem.setPolymerizationProblem(polymerizationProblem);
            problem = problemRepository.save(problem);
            return EntityConvertToDTOUtil.convertProblem(problem);
        } else {
            throw new BadParamException(ResultEnum.PARAM_WRONG);
        }
    }

    @Override
    @Transactional
    public ProblemDTO addImageInProblem(Long userId, ImageInProblemAddRequest imageInProblemAddRequest) throws IOException {
        // 为此问题加锁
        Optional<Problem> problemOptional = problemRepository.findByIdForUpdate(imageInProblemAddRequest.getProblemId());
        if(problemOptional.isEmpty()) {
            throw new BadParamException(ResultEnum.PROBLEM_NOT_EXIST);
        }
        Problem problem = problemOptional.get();
        if(!problem.getPaper().getCreator().getId().equals(userId)) {
            throw new UnauthorizedOperationException(userId, ResultEnum.NOT_CREATOR_EDIT_PAPER);
        }
        if(!problem.getPaper().getState().equals(PaperStateEnum.CREATING.getState())) {
            throw new NotAllowOperationException(ResultEnum.PAPER_STATE_IS_NOT_CREATING);
        }
        String imgName = UUID.randomUUID().toString();
        String images = problem.getImages();
        if(StringUtils.isEmpty(images)) {
            images = new JSONArray().toJSONString();
        }
        JSONArray jsonArray = JSONArray.parseArray(images);
        jsonArray.add(imageInProblemAddRequest.getIndex() + 1, imgName);
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
        if(problemOptional.isEmpty()) {
            throw new BadParamException(ResultEnum.PROBLEM_NOT_EXIST);
        }
        Problem problem = problemOptional.get();
        if(!problem.getPaper().getCreator().getId().equals(userId)) {
            throw new UnauthorizedOperationException(userId, ResultEnum.NOT_CREATOR_EDIT_PAPER);
        }
        if(!problem.getPaper().getState().equals(PaperStateEnum.CREATING.getState())) {
            throw new NotAllowOperationException(ResultEnum.PAPER_STATE_IS_NOT_CREATING);
        }
        String images = problem.getImages();
        if(StringUtils.isEmpty(images)) {
            images = new JSONArray().toJSONString();
        }
        JSONArray jsonArray = JSONArray.parseArray(images);
        String remove = (String) jsonArray.remove(imageInProblemDeleteRequest.getIndex() + 1);
        problem.setImages(jsonArray.toJSONString());
        problem = problemRepository.save(problem);
        imgFileRepository.deleteFile(remove);
        return EntityConvertToDTOUtil.convertProblem(problem);
    }

    @Override
    @Transactional
    public PaperDetailDTO deleteProblem(Long userId, ProblemDeleteRequest problemDeleteRequest) {
        // 为此试卷加锁
        Optional<Paper> paperOptional = paperRepository.findByIdForUpdate(problemDeleteRequest.getPaperId());
        if(paperOptional.isEmpty()) {
            throw new BadParamException(ResultEnum.PAPER_NOT_EXIST);
        }
        Paper paper = paperOptional.get();
        if(!paper.getCreator().getId().equals(userId)) {
            throw new UnauthorizedOperationException(userId, ResultEnum.NOT_CREATOR_EDIT_PAPER);
        }
        if(!paper.getState().equals(PaperStateEnum.CREATING.getState())) {
            throw new NotAllowOperationException(ResultEnum.PAPER_STATE_IS_NOT_CREATING);
        }
        Optional<Problem> problemOptional = problemRepository.findById(problemDeleteRequest.getProblemId());
        if(problemOptional.isEmpty()) {
            throw new BadParamException(ResultEnum.PROBLEM_NOT_EXIST);
        }
        Problem problem = problemOptional.get();
        if(problem.getPaper() == null || !problem.getPaper().getId().equals(paper.getId())) {
            //问题和试卷不相等
            throw new BadParamException(ResultEnum.PARAM_NOT_MATCH);
        }
        if(problem.getPolymerizationProblem() != null) {
            PolymerizationProblem polymerizationProblem = problem.getPolymerizationProblem();
            Set<Problem> problems = polymerizationProblem.getProblems();
            Map<Integer, Problem> problemMap = new HashMap<>();
            problems.forEach(problem1 -> {
                if(!problem1.getId().equals(problem.getId())) {
                    problemMap.put(problem1.getSort(), problem1);
                }
            });
            for(int index = 1, sort = 1; sort <= problemMap.size(); index++, sort++) {
                while(problemMap.get(index) == null) {
                    index++;
                }
                Problem p = problemMap.get(index);
                p.setSort(sort);
            }
            problems.remove(problem);
        } else {
            //todo 排序操作可以抽取
            Set<Problem> problems = paper.getProblems();
            Set<PolymerizationProblem> polymerizationProblems = paper.getPolymerizationProblems();
            Map<Integer, Object> problemMap = new HashMap<>();
            problems.forEach(problem1 -> {
                if(!problem1.getId().equals(problemDeleteRequest.getProblemId())) {
                    if(problem1.getPolymerizationProblem() == null) {
                        problemMap.put(problem1.getSort(), problem1);
                    }
                }
            });
            polymerizationProblems.forEach(polymerizationProblem ->
                    problemMap.put(polymerizationProblem.getSort(), polymerizationProblem));
            for(int index = 1, sort = 1; sort <= problemMap.size(); index++, sort++) {
                while(problemMap.get(index) == null) {
                    index++;
                }
                Object problem1 = problemMap.get(index);
                if(problem1 instanceof Problem) {
                    Problem p = (Problem) problem1;
                    p.setSort(sort);
                } else if(problem1 instanceof PolymerizationProblem) {
                    PolymerizationProblem p = (PolymerizationProblem) problem1;
                    p.setSort(sort);
                }
            }
            problems.remove(problem);
        }
        paper = paperRepository.save(paper);
        problemRepository.deleteById(problemDeleteRequest.getProblemId());
        String imagesJson = problem.getImages();
        JSONArray images = (JSONArray) JSONObject.parse(imagesJson);
        if(images != null) {
            imgFileRepository.deleteFiles(images.toJavaList(String.class));
        }
        return EntityConvertToDTOUtil.convertPaperToDetail(paper);
    }

    @Override
    @Transactional
    public PaperDetailDTO deletePolymerizationProblem(Long userId,
                                                PolymerizationProblemDeleteRequest polymerizationProblemDeleteRequest) {
        // 为此试卷加锁
        Optional<Paper> paperOptional = paperRepository.findByIdForUpdate(polymerizationProblemDeleteRequest.getPaperId());
        if(paperOptional.isEmpty()) {
            throw new BadParamException(ResultEnum.PAPER_NOT_EXIST);
        }
        // 鉴权
        Paper paper = paperOptional.get();
        if(!paper.getCreator().getId().equals(userId)) {
            throw new UnauthorizedOperationException(userId, ResultEnum.NOT_CREATOR_EDIT_PAPER);
        }
        if(!paper.getState().equals(PaperStateEnum.CREATING.getState())) {
            throw new NotAllowOperationException(ResultEnum.PAPER_STATE_IS_NOT_CREATING);
        }
        // 检查题目是否存在
        Optional<PolymerizationProblem> polymerizationProblemOptional =
                polymerizationProblemRepository.findById(polymerizationProblemDeleteRequest.getPolymerizationProblemId());
        if(polymerizationProblemOptional.isEmpty()) {
            throw new BadParamException(ResultEnum.POLYMERIZATION_PROBLEM_NOT_EXIST);
        }
        PolymerizationProblem polymerizationProblem1 = polymerizationProblemOptional.get();
        if(polymerizationProblem1.getPaper() == null ||
                !polymerizationProblem1.getPaper().getId().equals(paper.getId())) {
            throw new BadParamException(ResultEnum.PARAM_NOT_MATCH);
        }
        // 排序
        Set<Problem> problems = paper.getProblems();
        Set<PolymerizationProblem> polymerizationProblems = paper.getPolymerizationProblems();
        Map<Integer, Object> problemMap = new HashMap<>();
        problems.forEach(problem -> {
            if(problem.getPolymerizationProblem() == null) {
                problemMap.put(problem.getSort(), problem);
            }
        });
        polymerizationProblems.forEach(polymerizationProblem -> {
            if(!polymerizationProblem.getId().equals(polymerizationProblemDeleteRequest.getPolymerizationProblemId())) {
                problemMap.put(polymerizationProblem.getSort(), polymerizationProblem);
            }
        });
        problems.clear();
        polymerizationProblems.clear();
        for(int index = 1, sort = 1; sort <= problemMap.size(); index++, sort++) {
            while(problemMap.get(index) == null) {
                index++;
            }
            Object problem1 = problemMap.get(index);
            if(problem1 instanceof Problem) {
                Problem p = (Problem) problem1;
                p.setSort(sort);
                problems.add(p);
            } else if(problem1 instanceof PolymerizationProblem) {
                PolymerizationProblem p = (PolymerizationProblem) problem1;
                p.setSort(sort);
                polymerizationProblems.add(p);
            }
        }
        // 组织删除的图片
        List<String> deleteImg = new ArrayList<>();
        Set<Problem> deleteProblems = polymerizationProblem1.getProblems();
        deleteProblems.forEach(problem -> {
            JSONArray images = (JSONArray) JSONObject.parse(problem.getImages());
            if(images != null) {
                deleteImg.addAll(images.toJavaList(String.class));
            }
        });
        JSONArray images = (JSONArray) JSONObject.parse(polymerizationProblem1.getImages());
        paper = paperRepository.save(paper);
        problemRepository.deleteAllByPolymerizationProblem(polymerizationProblem1);
        polymerizationProblemRepository.deleteById(polymerizationProblemDeleteRequest.getPolymerizationProblemId());
        imgFileRepository.deleteFiles(deleteImg);
        if(images != null) {
            imgFileRepository.deleteFiles(images.toJavaList(String.class));
        }
        return EntityConvertToDTOUtil.convertPaperToDetail(paper);
    }

    @Override
    @Transactional
    public boolean deletePaper(Long userId, Long paperId) {
        // 为此试卷加锁
        Optional<Paper> paperOptional = paperRepository.findByIdForUpdate(paperId);
        if(paperOptional.isPresent()) {
            Paper paper = paperOptional.get();
            if(!paper.getCreator().getId().equals(userId)) {
                throw new UnauthorizedOperationException(userId, ResultEnum.NOT_CREATOR_EDIT_PAPER);
            }
            if(paper.getState().equals(PaperStateEnum.ANSWERING.getState())) {
                throw new NotAllowOperationException(ResultEnum.PAPER_STATE_CAN_NOT_DELETE);
            }
            Set<Problem> problems = paper.getProblems();
            List<String> deleteImg = new ArrayList<>();
            problems.forEach(problem -> {
                JSONArray images = (JSONArray) JSONObject.parse(problem.getImages());
                if(images != null) {
                    deleteImg.addAll(images.toJavaList(String.class));
                }
            });
            Set<PolymerizationProblem> polymerizationProblems = paper.getPolymerizationProblems();
            polymerizationProblems.forEach(polymerizationProblem -> {
                JSONArray images = (JSONArray) JSONObject.parse(polymerizationProblem.getImages());
                if(images != null) {
                    deleteImg.addAll(images.toJavaList(String.class));
                }
            });
            // todo 删除试卷需要删除答案
            problemRepository.deleteAllByPaper(paper);
            polymerizationProblemRepository.deleteAllByPaper(paper);
            paperRepository.deleteById(paper.getId());
            imgFileRepository.deleteFiles(deleteImg);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public PaperDetailDTO resortProblemsInPaper(Long userId, ProblemsInPaperResortRequest problemsInPaperResortRequest) {
        // 为此试卷加锁
        Optional<Paper> paperOptional = paperRepository.findByIdForUpdate(problemsInPaperResortRequest.getPaperId());
        if(paperOptional.isEmpty()) {
            return null;
        }
        Paper paper = paperOptional.get();
        if(!paper.getCreator().getId().equals(userId)) {
            throw new UnauthorizedOperationException(userId, ResultEnum.NOT_CREATOR_EDIT_PAPER);
        }
        if(!paper.getState().equals(PaperStateEnum.CREATING.getState())) {
            throw new NotAllowOperationException(ResultEnum.PAPER_STATE_IS_NOT_CREATING);
        }
        // 1.按照sort将所有大题放入map中
        Set<Problem> problems = paper.getProblems().stream()
                .filter(problem -> problem.getPolymerizationProblem() == null)
                .collect(Collectors.toSet());
        Set<PolymerizationProblem> polymerizationProblems = paper.getPolymerizationProblems();
        Map<Integer, Object> map = new HashMap<>();
        problems.forEach(problem -> map.put(problem.getSort(), problem));
        polymerizationProblems.forEach(polymerizationProblem ->
                map.put(polymerizationProblem.getSort(), polymerizationProblem));
        // 2.检查题目排序是否正确
        for (int i = 1; i <= map.size() ; i++) {
            if(!map.containsKey(i)) {
                // todo 可以做数据顺序恢复
                throw new DataDamageException("试卷中题目顺序异常 paperId:"+
                        problemsInPaperResortRequest.getPaperId().toString(), ResultEnum.DATA_WRONG);
            }
        }
        // 3.根据请求中的sort转换的map将原题目的sort进行更换
        problemsInPaperResortRequest.getOldIndexToNewIndexMap().forEach((oldSort, newSort) -> {
            Object problem = map.get(oldSort);
            if(problem != null) {
                if(map.containsKey(newSort)) {
                    if(problem instanceof Problem) {
                        ((Problem) problem).setSort(newSort);
                    } else if(problem instanceof PolymerizationProblem) {
                        ((PolymerizationProblem) problem).setSort(newSort);
                    }
                }
            }
        });
        // 4.检查更新后是否所有题目按照sort排还是连续的
        map.clear();
        problems.forEach(problem -> map.put(problem.getSort(), problem));
        polymerizationProblems.forEach(polymerizationProblem ->
                map.put(polymerizationProblem.getSort(), polymerizationProblem));
        for (int i = 1; i <= map.size() ; i++) {
            if(!map.containsKey(i)) {
                throw new BadParamException(ResultEnum.NEW_SORT_PARAM_WRONG);
            }
        }
        // 5. 保存
        paper = paperRepository.save(paper);
        return EntityConvertToDTOUtil.convertPaperToDetail(paper);
    }

    private final Set<String> stateChange = new HashSet<>();
    {
        stateChange.add(PaperStateEnum.CREATING.getState()+"-"+PaperStateEnum.READY_TO_ANSWERING.getState());
        stateChange.add(PaperStateEnum.READY_TO_ANSWERING.getState()+"-"+PaperStateEnum.CREATING.getState());
        stateChange.add(PaperStateEnum.ANSWERING.getState()+"-"+PaperStateEnum.END_ANSWER.getState());
        stateChange.add(PaperStateEnum.END_ANSWER.getState()+"-"+PaperStateEnum.ANSWERING.getState());
    }

    @Override
    public PaperDTO changePaperState(Long userId, PaperStateChangeRequest paperStateChangeRequest) {
        Optional<Paper> paperOptional = paperRepository.findByIdForUpdate(paperStateChangeRequest.getPaperId());
        if(paperOptional.isEmpty()) {
            throw new BadParamException(ResultEnum.PAPER_NOT_EXIST);
        }
        Paper paper = paperOptional.get();
        if(!paper.getCreator().getId().equals(userId)) {
            throw new UnauthorizedOperationException(userId, ResultEnum.NOT_CREATOR_EDIT_PAPER);
        }
        if(!stateChange.contains(paper.getState()+"-"+paperStateChangeRequest.getState())) {
            throw new NotAllowOperationException(ResultEnum.PAPER_STATE_CHANGE_NOT_ALLOW);
        }
        paper.setState(paperStateChangeRequest.getState());
        paper = paperRepository.save(paper);
        return EntityConvertToDTOUtil.convertPaper(paper);
    }

}
