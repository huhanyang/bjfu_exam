package com.bjfu.exam.controller;

import com.bjfu.exam.dto.paper.PaperDTO;
import com.bjfu.exam.dto.paper.PaperDetailDTO;
import com.bjfu.exam.dto.paper.PaperWithProblemsDTO;
import com.bjfu.exam.dto.paper.ProblemDTO;
import com.bjfu.exam.enums.ResultEnum;
import com.bjfu.exam.request.paper.*;
import com.bjfu.exam.security.annotation.RequireStudent;
import com.bjfu.exam.security.annotation.RequireTeacher;
import com.bjfu.exam.service.PaperService;
import com.bjfu.exam.util.DTOConvertToVOUtil;
import com.bjfu.exam.vo.BaseResult;
import com.bjfu.exam.vo.paper.PaperDetailVO;
import com.bjfu.exam.vo.paper.PaperVO;
import com.bjfu.exam.vo.paper.PaperWithProblemsVO;
import com.bjfu.exam.vo.paper.ProblemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.bjfu.exam.util.SessionUtil.getUserId;

@RestController
@Validated
@RequestMapping("/paper")
public class PaperController {

    @Autowired
    private PaperService paperService;

    @GetMapping("/getByCode")
    @RequireStudent
    public BaseResult<PaperDetailVO> getPaper(String code) {
        if(StringUtils.isEmpty(code)) {
            return new BaseResult<>(ResultEnum.PARAM_WRONG);
        }
        PaperDetailDTO paperDTO = paperService.getPaperByCode(code);
        if(paperDTO != null) {
            PaperDetailVO paperVO = DTOConvertToVOUtil.convertPaperDetailDTO(paperDTO);
            return new BaseResult<>(ResultEnum.SUCCESS, paperVO);
        }
        return new BaseResult<>(ResultEnum.FIND_FAILED);
    }

    @GetMapping("/getByPaperId")
    @RequireTeacher
    public BaseResult<PaperWithProblemsVO> getByPaperId(@NotNull(message = "试卷id不能为空!") Long paperId,
                                                        HttpSession session) {
        PaperWithProblemsDTO paperWithProblemsDTO =
                paperService.getPaperDetail(paperId, getUserId(session));
        if(paperWithProblemsDTO != null) {
            PaperWithProblemsVO paperWithProblemsVO = DTOConvertToVOUtil.convertPaperWithProblemsDTO(paperWithProblemsDTO);
            return new BaseResult<>(ResultEnum.SUCCESS, paperWithProblemsVO);
        }
        return new BaseResult<>(ResultEnum.FIND_FAILED);
    }

    @GetMapping("/get")
    @RequireTeacher
    public BaseResult<List<PaperVO>> getMyPaper(HttpSession session) {
        List<PaperDTO> paperDTOS = paperService.getAllPaperByCreatorId(getUserId(session));
        List<PaperVO> paperVOS = paperDTOS.stream()
                .map(DTOConvertToVOUtil::convertPaperDTO)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return new BaseResult<>(ResultEnum.SUCCESS, paperVOS);
    }

    @PutMapping("/createPaper")
    @RequireTeacher
    public BaseResult<PaperDetailVO> createPaper(@Validated @RequestBody PaperCreateRequest paperCreateRequest,
                                                 HttpSession session) {
        PaperDetailDTO paperDetailDTO = paperService.createPaper(paperCreateRequest, getUserId(session));
        PaperDetailVO paperDetailVO = DTOConvertToVOUtil.convertPaperDetailDTO(paperDetailDTO);
        return new BaseResult<>(ResultEnum.SUCCESS, paperDetailVO);
    }

    @PutMapping("/addProblem")
    @RequireTeacher
    public BaseResult<ProblemVO> addProblem(@Validated @RequestBody ProblemAddRequest problemAddRequest,
                                            HttpSession session) {
        Long userId = getUserId(session);
        ProblemDTO problemDTO = paperService.addProblem(userId, problemAddRequest);
        ProblemVO problemVO = DTOConvertToVOUtil.convertProblemDTO(problemDTO);
        return new BaseResult<>(ResultEnum.SUCCESS, problemVO);
    }

    @PutMapping("/addImageInProblem")
    @RequireTeacher
    public BaseResult<ProblemVO> addImageInProblem(ImageInProblemAddRequest imageInProblemAddRequest,
                                                   HttpSession session) throws IOException {
        Long userId = getUserId(session);
        ProblemDTO problemDTO = paperService.addImageInProblem(userId, imageInProblemAddRequest);
        ProblemVO problemVO = DTOConvertToVOUtil.convertProblemDTO(problemDTO);
        return new BaseResult<>(ResultEnum.SUCCESS, problemVO);
    }

    @DeleteMapping("/deleteImageInProblem")
    @RequireTeacher
    public BaseResult<ProblemVO> deleteImageInProblem(ImageInProblemDeleteRequest imageInProblemDeleteRequest,
                                                      HttpSession session) {
        Long userId = getUserId(session);
        ProblemDTO problemDTO = paperService.deleteImageInProblem(userId, imageInProblemDeleteRequest);
        ProblemVO problemVO = DTOConvertToVOUtil.convertProblemDTO(problemDTO);
        return new BaseResult<>(ResultEnum.SUCCESS, problemVO);
    }

    @DeleteMapping("/deleteProblem")
    @RequireTeacher
    public BaseResult<PaperDetailVO> deleteProblem(ProblemDeleteRequest problemDeleteRequest,
                                                   HttpSession session) {
        Long userId = getUserId(session);
        paperService.deleteProblem(userId, problemDeleteRequest);
        return new BaseResult<>(ResultEnum.SUCCESS);
    }

    @DeleteMapping("/delete")
    @RequireTeacher
    public BaseResult<Void> deletePaper(@NotNull(message = "试卷id不能为空!") Long paperId,
                                        HttpSession session) {
        Long userId = getUserId(session);
        paperService.deletePaper(userId, paperId);
        return new BaseResult<>(ResultEnum.SUCCESS);
    }

    @PostMapping("/changeState")
    @RequireTeacher
    public BaseResult<PaperVO> changePaperState(@Validated @RequestBody PaperStateChangeRequest paperStateChangeRequest,
                                                HttpSession session) {
        PaperDTO paperDTO = paperService.changePaperState(getUserId(session), paperStateChangeRequest);
        PaperVO paperVO = DTOConvertToVOUtil.convertPaperDTO(paperDTO);
        return new BaseResult<>(ResultEnum.SUCCESS, paperVO);
    }



}
