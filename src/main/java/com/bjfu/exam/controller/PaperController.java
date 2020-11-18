package com.bjfu.exam.controller;

import com.bjfu.exam.dto.paper.PaperDTO;
import com.bjfu.exam.dto.paper.PaperDetailDTO;
import com.bjfu.exam.dto.paper.PolymerizationProblemDetailDTO;
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
import com.bjfu.exam.vo.paper.PolymerizationProblemDetailVO;
import com.bjfu.exam.vo.paper.ProblemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/paper")
public class PaperController {

    @Autowired
    private PaperService paperService;

    @GetMapping("/getByCode")
    @RequireStudent
    public BaseResult<PaperVO> getPaper(String code) {
        if(StringUtils.isEmpty(code)) {
            return new BaseResult<>(ResultEnum.PARAM_WRONG);
        }
        PaperDTO paperDTO = paperService.getPaper(code);
        if(paperDTO != null) {
            PaperVO paperVO = DTOConvertToVOUtil.convertPaperDTO(paperDTO);
            return new BaseResult<>(ResultEnum.SUCCESS, paperVO);
        }
        return new BaseResult<>(ResultEnum.FIND_FAILED);
    }

    @GetMapping("/getByPaperId")
    @RequireTeacher
    public BaseResult<PaperDetailVO> getByPaperId(Long paperId, HttpSession session) {
        if(paperId == null) {
            return new BaseResult<>(ResultEnum.PARAM_WRONG);
        }
        PaperDetailDTO paperDetailDTO =
                paperService.getPaperDetail(paperId, (Long) session.getAttribute("userId"));
        if(paperDetailDTO != null) {
            PaperDetailVO paperDetailVO = DTOConvertToVOUtil.convertPaperDetailDTO(paperDetailDTO);
            return new BaseResult<>(ResultEnum.SUCCESS, paperDetailVO);
        }
        return new BaseResult<>(ResultEnum.FIND_FAILED);
    }

    @GetMapping("/get")
    @RequireTeacher
    public BaseResult<List<PaperDetailVO>> getMyPaper(HttpSession session) {
        List<PaperDetailDTO> paperDetailDTOS = paperService.getAllPaperByCreatorId((Long) session.getAttribute("userId"));
        List<PaperDetailVO> paperDetailVO = paperDetailDTOS.stream()
                .map(DTOConvertToVOUtil::convertPaperDetailDTO)
                .collect(Collectors.toList());
        return new BaseResult<>(ResultEnum.SUCCESS, paperDetailVO);
    }

    @PutMapping("/create")
    @RequireTeacher
    public BaseResult<PaperDetailVO> createPaper(@RequestBody PaperCreateRequest paperCreateRequest, HttpSession session) {
        if(!paperCreateRequest.isComplete()) {
            return new BaseResult<>(ResultEnum.PARAM_WRONG);
        }
        PaperDetailDTO paperDetailDTO = paperService.createPaper(paperCreateRequest, (Long) session.getAttribute("userId"));
        PaperDetailVO paperDetailVO = DTOConvertToVOUtil.convertPaperDetailDTO(paperDetailDTO);
        return new BaseResult<>(ResultEnum.SUCCESS, paperDetailVO);
    }

    @PutMapping("/addPolymerizationProblem")
    @RequireTeacher
    public BaseResult<PolymerizationProblemDetailVO> addPolymerizationProblem(@RequestBody PolymerizationProblemAddRequest polymerizationProblemAddRequest,
                                                                              HttpSession session) {
        if(!polymerizationProblemAddRequest.isComplete()) {
            return new BaseResult<>(ResultEnum.PARAM_WRONG);
        }
        Long userId = (Long) session.getAttribute("userId");
        PolymerizationProblemDetailDTO polymerizationProblemDetailDTO =
                paperService.addPolymerizationProblemInPaper(userId, polymerizationProblemAddRequest);
        if(polymerizationProblemDetailDTO == null) {
            return new BaseResult<>(ResultEnum.PARAM_NOT_MATCH);
        }
        PolymerizationProblemDetailVO polymerizationProblemDetailVO =
                DTOConvertToVOUtil.convertPolymerizationProblemDetailDTO(polymerizationProblemDetailDTO);
        return new BaseResult<>(ResultEnum.SUCCESS, polymerizationProblemDetailVO);
    }

    @PutMapping("/addImageInPolymerizationProblem")
    @RequireTeacher
    public BaseResult<PolymerizationProblemDetailVO> addImageInPolymerizationProblem(ImageInPolymerizationProblemAddRequest imageInPolymerizationProblemAddRequest,
                                                                                     HttpSession session) throws IOException {
        if(!imageInPolymerizationProblemAddRequest.isComplete()) {
            return new BaseResult<>(ResultEnum.PARAM_WRONG);
        }
        Long userId = (Long) session.getAttribute("userId");
        PolymerizationProblemDetailDTO polymerizationProblemDetailDTO =
                paperService.addImageInPolymerizationProblem(userId, imageInPolymerizationProblemAddRequest);
        if(polymerizationProblemDetailDTO == null) {
            return new BaseResult<>(ResultEnum.PARAM_NOT_MATCH);
        }
        PolymerizationProblemDetailVO polymerizationProblemDetailVO =
                DTOConvertToVOUtil.convertPolymerizationProblemDetailDTO(polymerizationProblemDetailDTO);
        return new BaseResult<>(ResultEnum.SUCCESS, polymerizationProblemDetailVO);
    }

    @DeleteMapping("/deleteImageInPolymerizationProblem")
    @RequireTeacher
    public BaseResult<PolymerizationProblemDetailVO> deleteImageInPolymerizationProblem(ImageInPolymerizationProblemDeleteRequest imageInPolymerizationProblemDeleteRequest,
                                                                                        HttpSession session) {
        if(!imageInPolymerizationProblemDeleteRequest.isComplete()) {
            return new BaseResult<>(ResultEnum.PARAM_WRONG);
        }
        Long userId = (Long) session.getAttribute("userId");
        PolymerizationProblemDetailDTO polymerizationProblemDetailDTO =
                paperService.deleteImageInPolymerizationProblem(userId, imageInPolymerizationProblemDeleteRequest);
        if(polymerizationProblemDetailDTO == null) {
            return new BaseResult<>(ResultEnum.PARAM_NOT_MATCH);
        }
        PolymerizationProblemDetailVO polymerizationProblemDetailVO = DTOConvertToVOUtil.convertPolymerizationProblemDetailDTO(polymerizationProblemDetailDTO);
        return new BaseResult<>(ResultEnum.SUCCESS, polymerizationProblemDetailVO);
    }

    @PutMapping("/addProblem")
    @RequireTeacher
    public BaseResult<ProblemVO> addProblem(@RequestBody ProblemAddRequest problemAddRequest, HttpSession session) {
        if(!problemAddRequest.isComplete()) {
            return new BaseResult<>(ResultEnum.PARAM_WRONG);
        }
        Long userId = (Long) session.getAttribute("userId");
        ProblemDTO problemDTO = paperService.addProblem(userId, problemAddRequest);
        if(problemDTO == null) {
            return new BaseResult<>(ResultEnum.PARAM_NOT_MATCH);
        }
        ProblemVO problemVO = DTOConvertToVOUtil.convertProblemDTO(problemDTO);
        return new BaseResult<>(ResultEnum.SUCCESS, problemVO);
    }

    @PutMapping("/addImageInProblem")
    @RequireTeacher
    public BaseResult<ProblemVO> addImageInProblem(ImageInProblemAddRequest imageInProblemAddRequest,
                                                   HttpSession session) throws IOException {
        if(!imageInProblemAddRequest.isComplete()) {
            return new BaseResult<>(ResultEnum.PARAM_WRONG);
        }
        Long userId = (Long) session.getAttribute("userId");
        ProblemDTO problemDTO = paperService.addImageInProblem(userId, imageInProblemAddRequest);
        if(problemDTO == null) {
            return new BaseResult<>(ResultEnum.PARAM_NOT_MATCH);
        }
        ProblemVO problemVO = DTOConvertToVOUtil.convertProblemDTO(problemDTO);
        return new BaseResult<>(ResultEnum.SUCCESS, problemVO);
    }

    @DeleteMapping("/deleteImageInProblem")
    @RequireTeacher
    public BaseResult<ProblemVO> deleteImageInProblem(ImageInProblemDeleteRequest imageInProblemDeleteRequest,
                                                      HttpSession session) {
        if(!imageInProblemDeleteRequest.isComplete()) {
            return new BaseResult<>(ResultEnum.PARAM_WRONG);
        }
        Long userId = (Long) session.getAttribute("userId");
        ProblemDTO problemDTO = paperService.deleteImageInProblem(userId, imageInProblemDeleteRequest);
        if(problemDTO == null) {
            return new BaseResult<>(ResultEnum.PARAM_NOT_MATCH);
        }
        ProblemVO problemVO = DTOConvertToVOUtil.convertProblemDTO(problemDTO);
        return new BaseResult<>(ResultEnum.SUCCESS, problemVO);
    }

    @DeleteMapping("/deleteProblem")
    @RequireTeacher
    public BaseResult<PaperDetailVO> deleteProblem(ProblemDeleteRequest problemDeleteRequest,
                                                   HttpSession session) {
        if(!problemDeleteRequest.isComplete()) {
            return new BaseResult<>(ResultEnum.PARAM_WRONG);
        }
        Long userId = (Long) session.getAttribute("userId");
        PaperDetailDTO paperDetailDTO = paperService.deleteProblem(userId, problemDeleteRequest);
        if(paperDetailDTO == null) {
            return new BaseResult<>(ResultEnum.PARAM_NOT_MATCH);
        }
        PaperDetailVO paperDetailVO = DTOConvertToVOUtil.convertPaperDetailDTO(paperDetailDTO);
        return new BaseResult<>(ResultEnum.SUCCESS, paperDetailVO);
    }

    @DeleteMapping("/deletePolymerizationProblem")
    @RequireTeacher
    public BaseResult<PaperDetailVO> deletePolymerizationProblem(PolymerizationProblemDeleteRequest polymerizationProblemDeleteRequest,
                                                                 HttpSession session) {
        if(!polymerizationProblemDeleteRequest.isComplete()) {
            return new BaseResult<>(ResultEnum.PARAM_WRONG);
        }
        Long userId = (Long) session.getAttribute("userId");
        PaperDetailDTO paperDetailDTO = paperService.deletePolymerizationProblem(userId, polymerizationProblemDeleteRequest);
        if(paperDetailDTO == null) {
            return new BaseResult<>(ResultEnum.PARAM_NOT_MATCH);
        }
        PaperDetailVO paperDetailVO = DTOConvertToVOUtil.convertPaperDetailDTO(paperDetailDTO);
        return new BaseResult<>(ResultEnum.SUCCESS, paperDetailVO);
    }

    @DeleteMapping("/delete")
    @RequireTeacher
    public BaseResult<Void> deletePaper(Long paperId, HttpSession session) {
        if(paperId == null) {
            return new BaseResult<>(ResultEnum.PARAM_WRONG);
        }
        Long userId = (Long) session.getAttribute("userId");
        boolean deletePaper = paperService.deletePaper(userId, paperId);
        if(!deletePaper) {
            return new BaseResult<>(ResultEnum.PARAM_NOT_MATCH);
        }
        return new BaseResult<>(ResultEnum.SUCCESS);
    }

    @PostMapping("/resortProblemsInPaper")
    @RequireTeacher
    public BaseResult<PaperDetailVO> resortProblemsInPaper(@RequestBody ProblemsInPaperResortRequest problemsInPaperResortRequest,
                                                           HttpSession session) {
        if(!problemsInPaperResortRequest.isComplete()) {
            return new BaseResult<>(ResultEnum.PARAM_WRONG);
        }
        PaperDetailDTO paperDetailDTO =
                paperService.resortProblemsInPaper((Long) session.getAttribute("userId"), problemsInPaperResortRequest);
        if(paperDetailDTO == null) {
            return new BaseResult<>(ResultEnum.PARAM_NOT_MATCH);
        }
        PaperDetailVO paperDetailVO = DTOConvertToVOUtil.convertPaperDetailDTO(paperDetailDTO);
        return new BaseResult<>(ResultEnum.SUCCESS, paperDetailVO);
    }

    @PostMapping("/changePaperState")
    @RequireTeacher
    public BaseResult<PaperVO> changePaperState(@RequestBody PaperStateChangeRequest paperStateChangeRequest,
                                                HttpSession session) {
        if(!paperStateChangeRequest.isComplete()) {
            return new BaseResult<>(ResultEnum.PARAM_WRONG);
        }
        PaperDTO paperDTO =
                paperService.changePaperState((Long) session.getAttribute("userId"), paperStateChangeRequest);
        PaperVO paperVO = DTOConvertToVOUtil.convertPaperDTO(paperDTO);
        return new BaseResult<>(ResultEnum.SUCCESS, paperVO);
    }



}
