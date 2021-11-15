package com.bjfu.exam.core.ao.impl;

import com.alibaba.fastjson.JSONObject;
import com.bjfu.exam.api.enums.ProblemTypeEnum;
import com.bjfu.exam.api.enums.ResultEnum;
import com.bjfu.exam.core.ao.ProblemAO;
import com.bjfu.exam.core.dto.answer.ProblemDTO;
import com.bjfu.exam.core.exception.BizException;
import com.bjfu.exam.core.params.problem.ProblemCreateProblemParams;
import com.bjfu.exam.core.params.problem.ProblemEditProblemParams;
import com.bjfu.exam.core.util.EntityConvertToDTOUtil;
import com.bjfu.exam.dao.entity.paper.Paper;
import com.bjfu.exam.dao.entity.paper.Problem;
import com.bjfu.exam.dao.repository.ImgFileRepository;
import com.bjfu.exam.dao.repository.paper.PaperRepository;
import com.bjfu.exam.dao.repository.paper.ProblemRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static com.bjfu.exam.api.enums.PaperStateEnum.SOFT_DELETE;

@Component
public class ProblemAOImpl implements ProblemAO {

    @Autowired
    private PaperRepository paperRepository;
    @Autowired
    private ProblemRepository problemRepository;
    @Autowired
    private ImgFileRepository imgFileRepository;

    @Override
    @Transactional
    public ProblemDTO createProblem(ProblemCreateProblemParams params, Long operatorId) {
        // 获取试卷
        Paper paper = getPaperWithCreatorId(getPaperWithLock(params.getPaperId()), operatorId);
        // 更新sort之后的题目的题号 依次加一
        List<Problem> needUpdateProblems = getNeedUpdateProblems(paper, params.getFatherProblemId(), params.getSort());
        needUpdateProblems.forEach(problem -> problem.setSort(problem.getSort() + 1));
        problemRepository.saveAll(needUpdateProblems);
        // 获取父题目
        Problem fatherProblem = null;
        if (params.getFatherProblemId() != null) {
            fatherProblem = problemRepository.findById(params.getFatherProblemId())
                    .filter(problem -> problem.getType().equals(ProblemTypeEnum.FATHER_PROBLEM))
                    .orElseThrow(() -> new BizException(ResultEnum.FATHER_PROBLEM_NOT_EXIST));
            // 防止复合题目嵌套
            if (params.getType().equals(ProblemTypeEnum.FATHER_PROBLEM)) {
                throw new BizException(ResultEnum.FATHER_PROBLEM_NOT_EXIST);
            }
        }
        // 创建新的题目并落库
        Problem problem = new Problem();
        BeanUtils.copyProperties(params, problem);
        problem.setFatherProblem(fatherProblem);
        problem.setSort(problemRepository.countByPaperAndFatherProblemIsNotNull(paper) + 1);
        problem = problemRepository.save(problem);
        // 返回新创建的题目
        return EntityConvertToDTOUtil.convertProblem(problem);
    }

    @Override
    public ProblemDTO editProblem(ProblemEditProblemParams params, Long operatorId) {
        // 获取试卷
        getPaperWithCreatorId(getPaper(params.getPaperId()), operatorId);
        // 获取题目
        Problem problem = problemRepository.findById(params.getProblemId())
                .orElseThrow(() -> new BizException(ResultEnum.PROBLEM_NOT_EXIST));
        // 更新题目并落库
        BeanUtils.copyProperties(params, problem);
        problem = problemRepository.save(problem);
        // 返回更新后的题目
        return EntityConvertToDTOUtil.convertProblem(problem);
    }

    @Override
    @Transactional
    public ProblemDTO addImageOnProblem(Long paperId, Long problemId, InputStream imageInputStream, Long operatorId) {
        // 获取试卷
        getPaperWithCreatorId(getPaper(paperId), operatorId);
        // 获取题目并加锁
        Problem problem = problemRepository.findByIdForUpdate(problemId)
                .orElseThrow(() -> new BizException(ResultEnum.PROBLEM_NOT_EXIST));
        // 解析图片
        List<String> images = Optional.ofNullable(problem.getImages())
                .map(imagesString -> JSONObject.parseArray(imagesString, String.class))
                .orElse(new ArrayList<>());
        // 上传新的图片
        String imageOssKey = UUID.randomUUID().toString();
        imgFileRepository.uploadFile(imageOssKey, imageInputStream);
        images.add(imageOssKey);
        // 更新图片列表并落库
        problem.setImages(JSONObject.toJSONString(images));
        problem = problemRepository.save(problem);
        // 返回更新后的题目
        return EntityConvertToDTOUtil.convertProblem(problem);
    }

    @Override
    @Transactional
    public void deleteProblem(Long paperId, Long problemId, Long operatorId) {
        // 获取试卷
        Paper paper = getPaperWithCreatorId(getPaperWithLock(paperId), operatorId);
        // 获取题目
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new BizException(ResultEnum.PROBLEM_NOT_EXIST));
        // 删除题目
        problemRepository.delete(problem);
        // 更新sort之后的题目的题号 依次减一
        Long fatherProblemId = Optional.ofNullable(problem.getFatherProblem())
                .map(Problem::getId)
                .orElse(null);
        List<Problem> needUpdateProblems = getNeedUpdateProblems(paper, fatherProblemId, problem.getSort());
        needUpdateProblems.forEach(problem1 -> problem1.setSort(problem1.getSort() - 1));
        problemRepository.saveAll(needUpdateProblems);
    }

    @Override
    @Transactional
    public ProblemDTO deleteImageOnProblem(Long paperId, Long problemId, Integer num, Long operatorId) {
        // 获取试卷
        getPaperWithCreatorId(getPaper(paperId), operatorId);
        // 获取题目并加锁
        Problem problem = problemRepository.findByIdForUpdate(problemId)
                .orElseThrow(() -> new BizException(ResultEnum.PROBLEM_NOT_EXIST));
        // 解析并删除图片
        List<String> images = Optional.ofNullable(problem.getImages())
                .map(imagesString -> JSONObject.parseArray(imagesString, String.class))
                .orElse(new ArrayList<>());
        if (images.size() > num) {
            String needRemoveImage = images.remove(num - 1);
            imgFileRepository.deleteFile(needRemoveImage);
        }
        // 更新图片列表并落库
        problem.setImages(JSONObject.toJSONString(images));
        problem = problemRepository.save(problem);
        // 返回更新后的题目
        return EntityConvertToDTOUtil.convertProblem(problem);
    }

    @Override
    public List<ProblemDTO> getProblems(Long paperId, Long operatorId) {
        // 获取试卷
        Paper paper = getPaperWithCreatorId(getPaper(paperId), operatorId);
        // 按照父题目分类 并排序所有子题目
        Map<Long, List<ProblemDTO>> subProblems = paper.getProblems()
                .stream()
                .filter(problem -> Objects.nonNull(problem.getFatherProblem()))
                .map(EntityConvertToDTOUtil::convertProblem)
                .collect(Collectors.groupingBy(problem -> problem.getFatherProblem().getId()));
        subProblems.forEach((fatherProblemId, problems) -> problems.sort(Comparator.comparing(ProblemDTO::getSort)));
        // 按照题号排序所有非子题目
        List<ProblemDTO> fatherProblems = paper.getProblems()
                .stream()
                .filter(problem -> Objects.isNull(problem.getFatherProblem()))
                .map(EntityConvertToDTOUtil::convertProblem)
                .sorted()
                .collect(Collectors.toList());
        // 填充子题目
        fatherProblems.forEach(fatherProblem -> fatherProblem.setSubProblems(subProblems.get(fatherProblem.getId())));
        return fatherProblems;
    }

    private Paper getPaper(Long paperId) {
        return paperRepository.findById(paperId)
                .filter(paper -> !paper.getState().equals(SOFT_DELETE))
                .orElseThrow(() -> new BizException(ResultEnum.PAPER_NOT_EXIST));
    }

    private Paper getPaperWithLock(Long paperId) {
        return paperRepository.findByIdForUpdate(paperId)
                .filter(paper -> !paper.getState().equals(SOFT_DELETE))
                .orElseThrow(() -> new BizException(ResultEnum.PAPER_NOT_EXIST));
    }

    private Paper getPaperWithCreatorId(Paper paper, Long creatorId) {
        return Optional.of(paper)
                .filter(paper1 -> paper1.getCreator().getId().equals(creatorId))
                .orElseThrow(() -> new BizException(ResultEnum.NOT_PAPER_CREATOR));
    }

    private List<Problem> getNeedUpdateProblems(Paper paper, Long fatherId, int sort) {
        if (Objects.isNull(fatherId)) {
            // 大于新题目序号的非子题目
            return paper.getProblems()
                    .stream()
                    .filter(problem -> Objects.isNull(problem.getFatherProblem()))
                    .filter(problem -> problem.getSort() > sort)
                    .collect(Collectors.toList());
        } else {
            // 大于新题目序号的同一父题目下的子题目
            return paper.getProblems()
                    .stream()
                    .filter(problem -> problem.getFatherProblem().getId().equals(fatherId))
                    .filter(problem -> problem.getSort() > sort)
                    .collect(Collectors.toList());
        }
    }
}
