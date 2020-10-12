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
import com.bjfu.exam.enums.UserTypeEnum;
import com.bjfu.exam.exception.UnauthorizedOperationException;
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
            return null;
        }
        List<Paper> papers = paperRepository.findAllByCreator(userOptional.get());
        List<PaperDetailDTO> paperDetailDTOS = papers.stream()
                .map(EntityConvertToDTOUtil::convertPaperToDetail)
                .collect(Collectors.toList());
        return paperDetailDTOS;
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
    @Transactional
    public PolymerizationProblemDTO addImageInPolymerizationProblem(Long userId,
                                                                    ImageInPolymerizationProblemAddRequest imageInPolymerizationProblemAddRequest) {
        Optional<PolymerizationProblem> polymerizationProblemOptional =
                polymerizationProblemRepository.findById(imageInPolymerizationProblemAddRequest.getPolymerizationProblemId());
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
            jsonArray.add(imageInPolymerizationProblemAddRequest.getIndex() + 1, url);
            polymerizationProblem.setImages(jsonArray.toJSONString());
            polymerizationProblem = polymerizationProblemRepository.save(polymerizationProblem);
            return EntityConvertToDTOUtil.convertPolymerizationProblem(polymerizationProblem);
        }
    }

    @Override
    @Transactional
    public PolymerizationProblemDTO deleteImageInPolymerizationProblem(Long userId, ImageInPolymerizationProblemDeleteRequest imageInPolymerizationProblemDeleteRequest) {
        Optional<PolymerizationProblem> polymerizationProblemOptional =
                polymerizationProblemRepository.findById(imageInPolymerizationProblemDeleteRequest.getPolymerizationProblemId());
        if(polymerizationProblemOptional.isEmpty()) {
            return null;
        } else {
            PolymerizationProblem polymerizationProblem = polymerizationProblemOptional.get();
            if(!polymerizationProblem.getPaper().getCreator().getId().equals(userId)) {
                return null;
            }
            String images = polymerizationProblem.getImages();
            if(StringUtils.isEmpty(images)) {
                images = new JSONArray().toJSONString();
            }
            JSONArray jsonArray = JSONArray.parseArray(images);
            // todo 删除图片
            jsonArray.remove(imageInPolymerizationProblemDeleteRequest.getIndex() + 1);
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

                //获取过滤


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
    @Transactional
    public ProblemDTO addImageInProblem(Long userId, ImageInProblemAddRequest imageInProblemAddRequest) {
        Optional<Problem> problemOptional = problemRepository.findById(imageInProblemAddRequest.getProblemId());
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
            jsonArray.add(imageInProblemAddRequest.getIndex() + 1, url);
            problem.setImages(jsonArray.toJSONString());
            problem = problemRepository.save(problem);
            return EntityConvertToDTOUtil.convertProblem(problem);
        }
    }

    @Override
    public ProblemDTO deleteImageInProblem(Long userId, ImageInProblemDeleteRequest imageInProblemDeleteRequest) {
        Optional<Problem> problemOptional = problemRepository.findById(imageInProblemDeleteRequest.getProblemId());
        if(problemOptional.isEmpty()) {
            return null;
        } else {
            Problem problem = problemOptional.get();
            if(!problem.getPaper().getCreator().getId().equals(userId)) {
                return null;
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
    }

    @Override
    @Transactional
    public PaperDetailDTO deleteProblem(Long userId, ProblemDeleteRequest problemDeleteRequest) {
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
            // todo 删除图片
            problemRepository.deleteById(problemDeleteRequest.getProblemId());
            return EntityConvertToDTOUtil.convertPaperToDetail(paper);
        }
    }

    @Override
    @Transactional
    public PaperDetailDTO deletePolymerizationProblem(Long userId,
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
            // todo 删除图片
            polymerizationProblemRepository.deleteById(polymerizationProblemDeleteRequest.getPolymerizationProblemId());
            return EntityConvertToDTOUtil.convertPaperToDetail(paper);
        }
    }

    @Override
    @Transactional
    public void deletePaper(Long userId, Long paperId) {
        Optional<Paper> paperOptional = paperRepository.findById(paperId);
        if(paperOptional.isPresent()) {
            Paper paper = paperOptional.get();
            if(paper.getCreator().getId().equals(userId)) {
                // todo 删除图片
                problemRepository.deleteAllByPaper(paper);
                polymerizationProblemRepository.deleteAllByPaper(paper);
                paperRepository.deleteById(paper.getId());
            }
        }
    }

    @Override
    @Transactional
    public PaperDetailDTO resortProblemsInPaper(Long userId, ProblemsInPaperResortRequest problemsInPaperResortRequest) {
        Optional<Paper> paperOptional = paperRepository.findById(problemsInPaperResortRequest.getPaperId());
        if(paperOptional.isEmpty()) {
            return null;
        }
        Paper paper = paperOptional.get();
        if(!paper.getCreator().getId().equals(userId)) {
            throw new UnauthorizedOperationException(userId, "非试卷创建者试图排序题号");
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
        // 2.根据请求中的sort转换的map将原题目的sort进行更换
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
        // 3.检查更新后是否所有题目按照sort排还是连续的
        map.clear();
        problems.forEach(problem -> map.put(problem.getSort(), problem));
        polymerizationProblems.forEach(polymerizationProblem ->
                map.put(polymerizationProblem.getSort(), polymerizationProblem));
        for (int i = 1; i <= map.size() ; i++) {
            if(!map.containsKey(i)) {
                return null;
            }
        }
        // 4. 保存
        paper = paperRepository.save(paper);
        return EntityConvertToDTOUtil.convertPaperToDetail(paper);
    }
}
