package com.bjfu.exam.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.bjfu.exam.dto.paper.PaperDTO;
import com.bjfu.exam.dto.paper.PaperDetailDTO;
import com.bjfu.exam.dto.paper.PolymerizationProblemDTO;
import com.bjfu.exam.dto.paper.ProblemDTO;
import com.bjfu.exam.entity.paper.Paper;
import com.bjfu.exam.entity.paper.PolymerizationProblem;
import com.bjfu.exam.entity.paper.Problem;
import com.bjfu.exam.entity.user.User;
import com.bjfu.exam.enums.PaperStateEnum;
import com.bjfu.exam.enums.ResponseBodyEnum;
import com.bjfu.exam.enums.UserTypeEnum;
import com.bjfu.exam.exception.*;
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

    @Override
    public PaperDTO getPaper(String code) {
        Optional<Paper> paperOptional = paperRepository.findByCode(code);
        if(paperOptional.isEmpty()) {
            return null;
        }
        return EntityConvertToDTOUtil.convertPaper(paperOptional.get());
    }

    @Override
    public List<PaperDetailDTO> getAllPaperByCreatorId(Long creatorId) {
        Optional<User> userOptional = userRepository.findById(creatorId);
        if(userOptional.isEmpty()) {
            throw new UserNotExistException(creatorId, ResponseBodyEnum.USER_NOT_EXIST);
        }
        List<Paper> papers = paperRepository.findAllByCreator(userOptional.get());
        List<PaperDetailDTO> paperDetailDTOS = papers.stream()
                .map(EntityConvertToDTOUtil::convertPaperToDetail)
                .collect(Collectors.toList());
        return paperDetailDTOS;
    }

    @Override
    @Transactional
    public PaperDetailDTO createPaper(PaperCreateRequest paperCreateRequest, Long creatorId) {
        Optional<User> userOptional = userRepository.findById(creatorId);
        if(userOptional.isEmpty()) {
            throw new UserNotExistException(creatorId, ResponseBodyEnum.USER_NOT_EXIST);
        }
        User creator = userOptional.get();
        if(!creator.getType().equals(UserTypeEnum.TEACHER.getType())) {
            throw new UnauthorizedOperationException(creatorId, ResponseBodyEnum.NOT_TEACHER_CREATE_PAPER);
        }
        Paper paper = new Paper();
        BeanUtils.copyProperties(paperCreateRequest, paper);
        paper.setCreator(creator);
        String code = RandomCodeUtil.nextCodeWithCharAndNumber();
        // todo 6位随机码的设计需要重新考虑 参考id生成
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
    public PolymerizationProblemDTO addPolymerizationProblemInPaper(Long userId,
                                                                    PolymerizationProblemAddRequest polymerizationProblemAddRequest) {
        // 为此试卷加锁
        paperRepository.existsById(polymerizationProblemAddRequest.getPaperId());
        Optional<Paper> paperOptional = paperRepository.findById(polymerizationProblemAddRequest.getPaperId());
        if(paperOptional.isEmpty()) {
            throw new BadParamException(ResponseBodyEnum.PAPER_NOT_EXIST);
        }
        Paper paper = paperOptional.get();
        if(!paper.getCreator().getId().equals(userId)) {
            throw new UnauthorizedOperationException(userId, ResponseBodyEnum.NOT_CREATOR_EDIT_PAPER);
        }
        int sort = paperRepository.getProblemSize(polymerizationProblemAddRequest.getPaperId()) +
                paperRepository.getPolymerizationProblemSize(polymerizationProblemAddRequest.getPaperId()) + 1;
        PolymerizationProblem polymerizationProblem = new PolymerizationProblem();
        BeanUtils.copyProperties(polymerizationProblemAddRequest, polymerizationProblem);
        polymerizationProblem.setSort(sort);
        polymerizationProblem.setPaper(paper);
        polymerizationProblem = polymerizationProblemRepository.save(polymerizationProblem);
        return EntityConvertToDTOUtil.convertPolymerizationProblem(polymerizationProblem);
    }

    @Override
    @Transactional
    public PolymerizationProblemDTO addImageInPolymerizationProblem(Long userId,
                                                                    ImageInPolymerizationProblemAddRequest imageInPolymerizationProblemAddRequest) {
        // 对此组合题目加锁
        Optional<PolymerizationProblem> polymerizationProblemOptional =
                polymerizationProblemRepository.findByIdForUpdate(imageInPolymerizationProblemAddRequest.getPolymerizationProblemId());
        if(polymerizationProblemOptional.isEmpty()) {
            throw new BadParamException(ResponseBodyEnum.POLYMERIZATION_PROBLEM_NOT_EXIST);
        }
        PolymerizationProblem polymerizationProblem = polymerizationProblemOptional.get();
        if(!polymerizationProblem.getPaper().getCreator().getId().equals(userId)) {
            throw new UnauthorizedOperationException(userId, ResponseBodyEnum.NOT_CREATOR_EDIT_PAPER);
        }
        // todo 保存图片获取保存位置url
        String url = "url";
        String images = polymerizationProblem.getImages();
        if(StringUtils.isEmpty(images)) {
            images = new JSONArray().toJSONString();
        }
        JSONArray jsonArray = JSONArray.parseArray(images);
        jsonArray.add(imageInPolymerizationProblemAddRequest.getIndex() - 1, url);
        polymerizationProblem.setImages(jsonArray.toJSONString());
        polymerizationProblem = polymerizationProblemRepository.save(polymerizationProblem);
        return EntityConvertToDTOUtil.convertPolymerizationProblem(polymerizationProblem);
    }

    @Override
    @Transactional
    public PolymerizationProblemDTO deleteImageInPolymerizationProblem(Long userId, ImageInPolymerizationProblemDeleteRequest imageInPolymerizationProblemDeleteRequest) {
        // 对此组合题目加锁
        Optional<PolymerizationProblem> polymerizationProblemOptional =
                polymerizationProblemRepository.findByIdForUpdate(imageInPolymerizationProblemDeleteRequest.getPolymerizationProblemId());
        if(polymerizationProblemOptional.isEmpty()) {
            throw new BadParamException(ResponseBodyEnum.POLYMERIZATION_PROBLEM_NOT_EXIST);
        }
        PolymerizationProblem polymerizationProblem = polymerizationProblemOptional.get();
        if(!polymerizationProblem.getPaper().getCreator().getId().equals(userId)) {
            throw new UnauthorizedOperationException(userId, ResponseBodyEnum.NOT_CREATOR_EDIT_PAPER);
        }
        String images = polymerizationProblem.getImages();
        if(StringUtils.isEmpty(images)) {
            images = new JSONArray().toJSONString();
        }
        JSONArray jsonArray = JSONArray.parseArray(images);
        // todo 删除图片
        jsonArray.remove(imageInPolymerizationProblemDeleteRequest.getIndex() - 1);
        polymerizationProblem.setImages(jsonArray.toJSONString());
        polymerizationProblem = polymerizationProblemRepository.save(polymerizationProblem);
        return EntityConvertToDTOUtil.convertPolymerizationProblem(polymerizationProblem);
    }

    @Override
    @Transactional
    public ProblemDTO addProblem(Long userId, ProblemAddRequest problemAddRequest) {
        // 为此试卷加锁
        Optional<Paper> paperOptional = paperRepository.findByIdForUpdate(problemAddRequest.getPaperId());
        if(paperOptional.isEmpty()) {
            throw new BadParamException(ResponseBodyEnum.PAPER_NOT_EXIST);
        }
        Paper paper = paperOptional.get();
        if(!paper.getCreator().getId().equals(userId)) {
            throw new UnauthorizedOperationException(userId, ResponseBodyEnum.NOT_CREATOR_EDIT_PAPER);
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
                throw new BadParamException(ResponseBodyEnum.POLYMERIZATION_PROBLEM_NOT_EXIST);
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
            throw new BadParamException(ResponseBodyEnum.PARAM_WRONG);
        }
    }

    @Override
    @Transactional
    public ProblemDTO addImageInProblem(Long userId, ImageInProblemAddRequest imageInProblemAddRequest) {
        // 为此问题加锁
        Optional<Problem> problemOptional = problemRepository.findByIdForUpdate(imageInProblemAddRequest.getProblemId());
        if(problemOptional.isEmpty()) {
            throw new BadParamException(ResponseBodyEnum.PROBLEM_NOT_EXIST);
        }
        Problem problem = problemOptional.get();
        if(!problem.getPaper().getCreator().getId().equals(userId)) {
            throw new UnauthorizedOperationException(userId, ResponseBodyEnum.NOT_CREATOR_EDIT_PAPER);
        }
        // todo 保存图片获取保存位置url
        String url = "url";
        String images = problem.getImages();
        if(StringUtils.isEmpty(images)) {
            images = new JSONArray().toJSONString();
        }
        JSONArray jsonArray = JSONArray.parseArray(images);
        jsonArray.add(imageInProblemAddRequest.getIndex() + 1, url);
        problem.setImages(jsonArray.toJSONString());
        problem = problemRepository.save(problem);
        return EntityConvertToDTOUtil.convertProblem(problem);
    }

    @Override
    @Transactional
    public ProblemDTO deleteImageInProblem(Long userId, ImageInProblemDeleteRequest imageInProblemDeleteRequest) {
        // 为此问题加锁
        Optional<Problem> problemOptional = problemRepository.findByIdForUpdate(imageInProblemDeleteRequest.getProblemId());
        if(problemOptional.isEmpty()) {
            throw new BadParamException(ResponseBodyEnum.PROBLEM_NOT_EXIST);
        }
        Problem problem = problemOptional.get();
        if(!problem.getPaper().getCreator().getId().equals(userId)) {
            throw new UnauthorizedOperationException(userId, ResponseBodyEnum.NOT_CREATOR_EDIT_PAPER);
        }
        String images = problem.getImages();
        if(StringUtils.isEmpty(images)) {
            images = new JSONArray().toJSONString();
        }
        JSONArray jsonArray = JSONArray.parseArray(images);
        // todo 删除图片
        jsonArray.remove(imageInProblemDeleteRequest.getIndex() + 1);
        problem.setImages(jsonArray.toJSONString());
        problem = problemRepository.save(problem);
        return EntityConvertToDTOUtil.convertProblem(problem);
    }

    @Override
    @Transactional
    public PaperDetailDTO deleteProblem(Long userId, ProblemDeleteRequest problemDeleteRequest) {
        // 为此试卷加锁
        Optional<Paper> paperOptional = paperRepository.findByIdForUpdate(problemDeleteRequest.getPaperId());
        if(paperOptional.isEmpty()) {
            throw new BadParamException(ResponseBodyEnum.PAPER_NOT_EXIST);
        }
        Paper paper = paperOptional.get();
        if(!paper.getCreator().getId().equals(userId)) {
            throw new UnauthorizedOperationException(userId, ResponseBodyEnum.NOT_CREATOR_EDIT_PAPER);
        }
        Optional<Problem> problemOptional = problemRepository.findById(problemDeleteRequest.getProblemId());
        if(problemOptional.isEmpty()) {
            throw new BadParamException(ResponseBodyEnum.PROBLEM_NOT_EXIST);
        }
        Problem problem = problemOptional.get();
        if(problem.getPaper() == null || !problem.getPaper().getId().equals(paper.getId())) {
            //问题和试卷不相等
            throw new BadParamException(ResponseBodyEnum.PARAM_NOT_MATCH);
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
        // todo 删除图片
        problemRepository.deleteById(problemDeleteRequest.getProblemId());
        return EntityConvertToDTOUtil.convertPaperToDetail(paper);
    }

    @Override
    @Transactional
    public PaperDetailDTO deletePolymerizationProblem(Long userId,
                                                PolymerizationProblemDeleteRequest polymerizationProblemDeleteRequest) {
        // 为此试卷加锁
        Optional<Paper> paperOptional = paperRepository.findByIdForUpdate(polymerizationProblemDeleteRequest.getPaperId());
        if(paperOptional.isEmpty()) {
            throw new BadParamException(ResponseBodyEnum.PAPER_NOT_EXIST);
        }
        Paper paper = paperOptional.get();
        if(!paper.getCreator().getId().equals(userId)) {
            throw new UnauthorizedOperationException(userId, ResponseBodyEnum.NOT_CREATOR_EDIT_PAPER);
        }
        Optional<PolymerizationProblem> polymerizationProblemOptional =
                polymerizationProblemRepository.findById(polymerizationProblemDeleteRequest.getPolymerizationProblemId());
        if(polymerizationProblemOptional.isEmpty()) {
            throw new BadParamException(ResponseBodyEnum.POLYMERIZATION_PROBLEM_NOT_EXIST);
        }
        PolymerizationProblem polymerizationProblem1 = polymerizationProblemOptional.get();
        if(polymerizationProblem1.getPaper() == null ||
                !polymerizationProblem1.getPaper().getId().equals(paper.getId())) {
            throw new BadParamException(ResponseBodyEnum.PARAM_NOT_MATCH);
        }
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
        paper = paperRepository.save(paper);
        problemRepository.deleteAllByPolymerizationProblem(polymerizationProblem1);
        // todo 删除图片
        polymerizationProblemRepository.deleteById(polymerizationProblemDeleteRequest.getPolymerizationProblemId());
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
                throw new UnauthorizedOperationException(userId, ResponseBodyEnum.NOT_CREATOR_EDIT_PAPER);
            }
            // todo 删除图片
            problemRepository.deleteAllByPaper(paper);
            polymerizationProblemRepository.deleteAllByPaper(paper);
            paperRepository.deleteById(paper.getId());
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
            throw new UnauthorizedOperationException(userId, ResponseBodyEnum.NOT_CREATOR_EDIT_PAPER);
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
                        problemsInPaperResortRequest.getPaperId().toString(), ResponseBodyEnum.DATA_WRONG);
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
                throw new BadParamException(ResponseBodyEnum.NEW_SORT_PARAM_WRONG);
            }
        }
        // 5. 保存
        paper = paperRepository.save(paper);
        return EntityConvertToDTOUtil.convertPaperToDetail(paper);
    }

    @Override
    public PaperDTO changePaperState(Long userId, PaperStateChangeRequest paperStateChangeRequest) {
        Optional<Paper> paperOptional = paperRepository.findByIdForUpdate(paperStateChangeRequest.getPaperId());
        if(paperOptional.isEmpty()) {
            throw new BadParamException(ResponseBodyEnum.PAPER_NOT_EXIST);
        }
        Paper paper = paperOptional.get();
        if(!paper.getCreator().getId().equals(userId)) {
            throw new UnauthorizedOperationException(userId, ResponseBodyEnum.NOT_CREATOR_EDIT_PAPER);
        }
        if(paper.getState() == PaperStateEnum.ANSWERING.getState()
                && paperStateChangeRequest.getState() == PaperStateEnum.CREATING.getState()) {
            throw new NotAllowOperationException(ResponseBodyEnum.PAPER_STATE_FROM_ANSWERING_TO_CREATING);
        }
        if(paper.getState() == PaperStateEnum.DELETE.getState()) {
            throw new NotAllowOperationException(ResponseBodyEnum.PAPER_STATE_FROM_DELETE_TO_OTHERS);
        }
        paper.setState(paperStateChangeRequest.getState());
        paper = paperRepository.save(paper);
        PaperDTO paperDTO = EntityConvertToDTOUtil.convertPaper(paper);
        return paperDTO;
    }
}
