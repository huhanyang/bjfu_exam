package com.bjfu.exam.core.ao.impl;

import com.bjfu.exam.api.bo.Page;
import com.bjfu.exam.api.enums.PaperStateEnum;
import com.bjfu.exam.api.enums.ResultEnum;
import com.bjfu.exam.core.ao.PaperAO;
import com.bjfu.exam.core.dto.paper.PaperDTO;
import com.bjfu.exam.core.dto.paper.PaperDetailsDTO;
import com.bjfu.exam.core.exception.BizException;
import com.bjfu.exam.core.params.paper.PaperCreatePaperParams;
import com.bjfu.exam.core.params.paper.PaperEditPaperParams;
import com.bjfu.exam.core.params.paper.PaperListCreatedPapersParams;
import com.bjfu.exam.core.util.ConvertUtil;
import com.bjfu.exam.core.util.EntityConvertToDTOUtil;
import com.bjfu.exam.dao.entity.paper.Paper;
import com.bjfu.exam.dao.entity.user.User;
import com.bjfu.exam.dao.repository.paper.PaperRepository;
import com.bjfu.exam.dao.repository.user.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;

import static com.bjfu.exam.api.enums.PaperStateEnum.*;

@Component
public class PaperAOImpl implements PaperAO {

    @Autowired
    private PaperRepository paperRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public PaperDTO createPaper(PaperCreatePaperParams params, Long operatorId) {
        // 查找用户
        User creator = userRepository.findById(operatorId)
                .orElseThrow(() -> new BizException(ResultEnum.USER_NOT_EXIST));
        // 创建并落库新的试卷
        Paper paper = new Paper();
        BeanUtils.copyProperties(params, paper);
        paper.setCreator(creator);
        paper.setState(CREATING);
        paper = paperRepository.save(paper);
        // 返回创建的试卷
        return EntityConvertToDTOUtil.convertPaper(paper);
    }

    @Override
    public PaperDTO editPaper(PaperEditPaperParams params, Long operatorId) {
        // 查找试卷
        Paper paper = getPaperWithCreatorId(params.getPaperId(), operatorId);
        // 试卷状态必须为创建状态方可修改
        if (!paper.getState().equals(PaperStateEnum.CREATING)) {
            throw new BizException(ResultEnum.PAPER_STATE_IS_NOT_CREATING);
        }
        // 修改并落库试卷
        BeanUtils.copyProperties(paper, paper);
        paper = paperRepository.save(paper);
        // 返回修改后的试卷
        return EntityConvertToDTOUtil.convertPaper(paper);
    }

    private static final Map<PaperStateEnum, Set<PaperStateEnum>> stateTransferMap = new HashMap<>();

    static {
        stateTransferMap.put(CREATING, Set.of(READY_TO_ANSWERING, SOFT_DELETE));
        stateTransferMap.put(READY_TO_ANSWERING, Set.of(CREATING, ANSWERING, SOFT_DELETE));
        stateTransferMap.put(ANSWERING, Set.of(END_ANSWER, SOFT_DELETE));
        stateTransferMap.put(END_ANSWER, Set.of(ANSWERING, SOFT_DELETE));
    }

    @Override
    @Transactional
    public PaperDTO changePaperState(Long paperId, PaperStateEnum newState, Long operatorId) {
        // 查找试卷
        Paper paper = getPaperWithCreatorId(paperId, operatorId);
        // 对试卷加锁
        paper = paperRepository.findByIdForUpdate(paper.getId())
                .filter(paper1 -> !paper1.getState().equals(SOFT_DELETE))
                .orElseThrow(() -> new BizException(ResultEnum.PAPER_NOT_EXIST));
        // 判断状态转移是否允许
        if (!stateTransferMap.getOrDefault(paper.getState(), Collections.emptySet()).contains(newState)) {
            throw new BizException(ResultEnum.PAPER_STATE_CHANGE_NOT_ALLOW);
        }
        // 给试卷生成作答用的code
        if (newState.equals(READY_TO_ANSWERING) && Objects.isNull(paper.getState())) {
            paper.setCode(UUID.randomUUID().toString());
        }
        // 修改试卷状态状态并落库
        paper.setState(newState);
        paper = paperRepository.save(paper);
        // 返回修改后的试卷
        return EntityConvertToDTOUtil.convertPaper(paper);
    }

    @Override
    public PaperDTO getPaperByCode(String code) {
        // 通过code查找试卷
        Paper paper = paperRepository.findByCode(code)
                .filter(paper1 -> !paper1.getState().equals(SOFT_DELETE))
                .orElseThrow(() -> new BizException(ResultEnum.PAPER_NOT_EXIST));
        return EntityConvertToDTOUtil.convertPaper(paper);
    }

    @Override
    public PaperDetailsDTO getCreatedPaperDetails(Long paperId, Long operatorId) {
        // 查找试卷
        Paper paper = getPaperWithCreatorId(paperId, operatorId);
        // 返回试卷详情
        return EntityConvertToDTOUtil.convertPaperToDetails(paper);
    }

    @Override
    public PaperDTO getCreatedPaper(Long paperId, Long operatorId) {
        // 查找试卷
        Paper paper = getPaperWithCreatorId(paperId, operatorId);
        // 返回试卷
        return EntityConvertToDTOUtil.convertPaper(paper);
    }

    @Override
    public Page<PaperDTO> listCreatedPapers(PaperListCreatedPapersParams params, Long creatorId) {
        // 查找用户
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new BizException(ResultEnum.USER_NOT_EXIST));
        // 分页查询
        PageRequest pageRequest = PageRequest.of(params.getPage(), params.getSize());
        org.springframework.data.domain.Page<Paper> paperPage =
                paperRepository.findAllByCreatorAndStateOrderByCreatedTimeDesc(creator, params.getState(), pageRequest);
        return ConvertUtil.convertJpaPageToExamPage(paperPage, EntityConvertToDTOUtil::convertPaper);
    }

    private Paper getPaper(Long paperId) {
        return paperRepository.findById(paperId)
                .filter(paper -> !paper.getState().equals(SOFT_DELETE))
                .orElseThrow(() -> new BizException(ResultEnum.PAPER_NOT_EXIST));
    }

    private Paper getPaperWithCreatorId(Long paperId, Long creatorId) {
        return Optional.of(getPaper(paperId))
                .filter(paper -> paper.getCreator().getId().equals(creatorId))
                .orElseThrow(() -> new BizException(ResultEnum.NOT_PAPER_CREATOR));
    }
}
