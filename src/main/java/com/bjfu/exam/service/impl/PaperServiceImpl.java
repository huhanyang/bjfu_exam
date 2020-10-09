package com.bjfu.exam.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.bjfu.exam.dto.PaperDTO;
import com.bjfu.exam.dto.PolymerizationProblemDTO;
import com.bjfu.exam.dto.ProblemDTO;
import com.bjfu.exam.entity.paper.Paper;
import com.bjfu.exam.entity.paper.PolymerizationProblem;
import com.bjfu.exam.entity.paper.Problem;
import com.bjfu.exam.entity.user.User;
import com.bjfu.exam.enums.UserTypeEnum;
import com.bjfu.exam.exception.UnauthorizedOperationException;
import com.bjfu.exam.repository.paper.PaperRepository;
import com.bjfu.exam.repository.paper.PolymerizationProblemRepository;
import com.bjfu.exam.repository.paper.ProblemRepository;
import com.bjfu.exam.repository.user.UserRepository;
import com.bjfu.exam.request.*;
import com.bjfu.exam.service.PaperService;
import com.bjfu.exam.util.EntityConvertToDTOUtil;
import com.bjfu.exam.util.RandomCodeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.io.File;
import java.util.*;

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
    public List<PaperDTO> getAllPaperByCreatorId(Long creatorId) {
        Optional<User> userOptional = userRepository.findById(creatorId);
        if(userOptional.isEmpty()) {
            return null;
        }
        Iterable<Paper> paperIterable = paperRepository.findAllByCreator(userOptional.get());
        List<PaperDTO> paperDTOS = new ArrayList<>();
        paperIterable.forEach(paper -> {
            paperDTOS.add(EntityConvertToDTOUtil.convertPaper(paper));
        });
        return paperDTOS;
    }

    @Override
    @Transactional
    public PaperDTO createPaper(PaperCreateRequest paperCreateRequest, Long creatorId) {
        Optional<User> userOptional = userRepository.findById(creatorId);
        if(userOptional.isEmpty()) {
            return null;
        }
        User creator = userOptional.get();
        if(!creator.getType().equals(UserTypeEnum.TEACHER.getType())) {
            throw new UnauthorizedOperationException(creatorId, "非教师用户试图创建试卷");
        }
        Paper paper = new Paper();
        BeanUtils.copyProperties(paperCreateRequest, paper);
        paper.setCreator(creator);
        String code = RandomCodeUtil.nextCodeWithCharAndNumber();
        while(paperRepository.existsByCode(code)) {
            code = RandomCodeUtil.nextCodeWithCharAndNumber();
        }
        paper.setCode(code);
        paper = paperRepository.save(paper);
        return EntityConvertToDTOUtil.convertPaper(paper);
    }

    @Override
    @Transactional
    public PolymerizationProblemDTO addPolymerizationProblemInPaper(Long userId,
                                                                    PolymerizationProblemAddRequest polymerizationProblemAddRequest) {
        Optional<Paper> paperOptional = paperRepository.findById(polymerizationProblemAddRequest.getPaperId());
        if(paperOptional.isEmpty()) {
            return null;
        } else {
            Paper paper = paperOptional.get();
            if(!paper.getCreator().getId().equals(userId)) {
                throw new UnauthorizedOperationException(userId, "非创建者添加组合题目");
            }
            int sort = paperRepository.getProblemSize(polymerizationProblemAddRequest.getPaperId()) +
                    paperRepository.getPolymerizationProblemSize(polymerizationProblemAddRequest.getPaperId()) +1;
            PolymerizationProblem polymerizationProblem = new PolymerizationProblem();
            BeanUtils.copyProperties(polymerizationProblemAddRequest, polymerizationProblem);
            polymerizationProblem.setSort(sort);
            polymerizationProblem.setPaper(paper);
            polymerizationProblem = polymerizationProblemRepository.save(polymerizationProblem);
            return EntityConvertToDTOUtil.convertPolymerizationProblem(polymerizationProblem);
        }
    }

    @Override
    public PolymerizationProblemDTO addImageInPolymerizationProblem(Long userId,
                                                                 Long polymerizationProblemId, File image) {
        Optional<PolymerizationProblem> polymerizationProblemOptional =
                polymerizationProblemRepository.findById(polymerizationProblemId);
        if(polymerizationProblemOptional.isEmpty()) {
            return null;
        } else {
            PolymerizationProblem polymerizationProblem = polymerizationProblemOptional.get();
            if(!polymerizationProblem.getPaper().getCreator().getId().equals(userId)) {
                return null;
            }
            // todo 保存图片获取保存位置url
            String url = "url";
            String images = polymerizationProblem.getImages();
            if(StringUtils.isEmpty(images)) {
                images = new JSONArray().toJSONString();
            }
            JSONArray jsonArray = JSONArray.parseArray(images);
            jsonArray.add(url);
            polymerizationProblem.setImages(jsonArray.toJSONString());
            polymerizationProblem = polymerizationProblemRepository.save(polymerizationProblem);
            return EntityConvertToDTOUtil.convertPolymerizationProblem(polymerizationProblem);
        }
    }

    @Override
    @Transactional
    public ProblemDTO addProblem(Long userId, ProblemAddRequest problemAddRequest) {
        Optional<Paper> paperOptional = paperRepository.findById(problemAddRequest.getPaperId());
        if(paperOptional.isEmpty()) {
            return null;
        } else {
            Paper paper = paperOptional.get();
            if(!paper.getCreator().getId().equals(userId)) {
                throw new UnauthorizedOperationException(userId, "非创建者添加题目");
            }
            Problem problem = new Problem();
            if(problemAddRequest.getPaperId() != null && problemAddRequest.getPolymerizationProblemId() == null) {
                int sort = paperRepository.getProblemSize(problemAddRequest.getPaperId()) +
                        paperRepository.getPolymerizationProblemSize(problemAddRequest.getPaperId()) +1;
                BeanUtils.copyProperties(problemAddRequest, problem);
                problem.setSort(sort);
                problem.setPaper(paper);
                problem = problemRepository.save(problem);
                return EntityConvertToDTOUtil.convertProblem(problem);
            } else if(problemAddRequest.getPaperId() != null && problemAddRequest.getPolymerizationProblemId() != null){
                Optional<PolymerizationProblem> polymerizationProblemOptional = polymerizationProblemRepository
                        .findById(problemAddRequest.getPolymerizationProblemId());
                if(polymerizationProblemOptional.isEmpty()) {
                    return null;
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
                return null;
            }
        }
    }

    @Override
    public ProblemDTO addImageInProblem(Long userId, Long problemId, File image) {
        Optional<Problem> problemOptional = problemRepository.findById(problemId);
        if(problemOptional.isEmpty()) {
            return null;
        } else {
            Problem problem = problemOptional.get();
            if(!problem.getPaper().getCreator().getId().equals(userId)) {
                return null;
            }
            // todo 保存图片获取保存位置url
            String url = "url";
            String images = problem.getImages();
            if(StringUtils.isEmpty(images)) {
                images = new JSONArray().toJSONString();
            }
            JSONArray jsonArray = JSONArray.parseArray(images);
            jsonArray.add(url);
            problem.setImages(jsonArray.toJSONString());
            problem = problemRepository.save(problem);
            return EntityConvertToDTOUtil.convertProblem(problem);
        }
    }

    @Override
    @Transactional
    public PaperDTO deleteProblem(Long userId, ProblemDeleteRequest problemDeleteRequest) {
        Optional<Paper> paperOptional = paperRepository.findById(problemDeleteRequest.getPaperId());
        if(paperOptional.isEmpty()) {
            return null;
        } else {
            Paper paper = paperOptional.get();
            Optional<Problem> problemOptional = problemRepository.findById(problemDeleteRequest.getProblemId());
            if(problemOptional.isEmpty()) {
                return null;
            }
            Problem problem = problemOptional.get();
            if(problem.getPaper() == null || !problem.getPaper().getId().equals(paper.getId())) {
                return null;
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
                problems.remove(problem);///////
            } else {
                if(!paper.getCreator().getId().equals(userId)) {
                    throw new UnauthorizedOperationException(userId, "非创建者删除题目");
                }
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
            return EntityConvertToDTOUtil.convertPaper(paper);
        }
    }

    @Override
    @Transactional
    public PaperDTO deletePolymerizationProblem(Long userId,
                                                PolymerizationProblemDeleteRequest polymerizationProblemDeleteRequest) {
        Optional<Paper> paperOptional = paperRepository.findById(polymerizationProblemDeleteRequest.getPaperId());
        if(paperOptional.isEmpty()) {
            return null;
        } else {
            Paper paper = paperOptional.get();
            if(!paper.getCreator().getId().equals(userId)) {
                return null;
            }
            Optional<PolymerizationProblem> polymerizationProblemOptional =
                    polymerizationProblemRepository.findById(polymerizationProblemDeleteRequest.getPolymerizationProblemId());
            if(polymerizationProblemOptional.isEmpty()) {
                return null;
            }
            PolymerizationProblem polymerizationProblem1 = polymerizationProblemOptional.get();
            if(polymerizationProblem1.getPaper() == null ||
                    !polymerizationProblem1.getPaper().getId().equals(paper.getId())) {
                return null;
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
            polymerizationProblemRepository.deleteById(polymerizationProblemDeleteRequest.getPolymerizationProblemId());
            return EntityConvertToDTOUtil.convertPaper(paper);
        }
    }

    @Override
    @Transactional
    public void deletePaper(Long userId, Long paperId) {
        Optional<Paper> paperOptional = paperRepository.findById(paperId);
        if(paperOptional.isPresent()) {
            Paper paper = paperOptional.get();
            if(paper.getCreator().getId().equals(userId)) {
                problemRepository.deleteAllByPaper(paper);
                polymerizationProblemRepository.deleteAllByPaper(paper);
                paperRepository.deleteById(paper.getId());
            }
        }
    }
}
