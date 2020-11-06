package com.bjfu.exam.controller;

import com.bjfu.exam.dto.paper.PaperDTO;
import com.bjfu.exam.dto.paper.PaperDetailDTO;
import com.bjfu.exam.dto.paper.PolymerizationProblemDTO;
import com.bjfu.exam.dto.paper.ProblemDTO;
import com.bjfu.exam.enums.ResponseBodyEnum;
import com.bjfu.exam.request.paper.*;
import com.bjfu.exam.service.PaperService;
import com.bjfu.exam.util.DTOConvertToVOUtil;
import com.bjfu.exam.util.SessionUtil;
import com.bjfu.exam.vo.ResponseBody;
import com.bjfu.exam.vo.paper.PaperDetailVO;
import com.bjfu.exam.vo.paper.PaperVO;
import com.bjfu.exam.vo.paper.PolymerizationProblemVO;
import com.bjfu.exam.vo.paper.ProblemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/paper")
public class PaperController {

    @Autowired
    private PaperService paperService;

    @GetMapping("/getByCode")
    public ResponseBody<PaperVO> getPaper(String code, HttpSession session) {
        if(StringUtils.isEmpty(code)) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
        }
        if(!SessionUtil.existSession(session)) {
            return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
        }
        PaperDTO paperDTO = paperService.getPaper(code);
        if(paperDTO != null) {
            PaperVO paperVO = DTOConvertToVOUtil.convertPaperDTO(paperDTO);
            return new ResponseBody<>(ResponseBodyEnum.SUCCESS, paperVO);
        }
        return new ResponseBody<>(ResponseBodyEnum.FIND_FAILED);
    }

    @GetMapping("/getByPaperId")
    public ResponseBody<PaperDetailVO> getByPaperId(Long paperId, HttpSession session) {
        if(paperId == null) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
        }
        if(!SessionUtil.existSession(session)) {
            return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
        }
        PaperDetailDTO paperDetailDTO =
                paperService.getPaperDetail(paperId, (Long) session.getAttribute("userId"));
        if(paperDetailDTO != null) {
            PaperDetailVO paperDetailVO = DTOConvertToVOUtil.convertPaperDetailDTO(paperDetailDTO);
            return new ResponseBody<>(ResponseBodyEnum.SUCCESS, paperDetailVO);
        }
        return new ResponseBody<>(ResponseBodyEnum.FIND_FAILED);
    }

    @GetMapping("/get")
    public ResponseBody<List<PaperDetailVO>> getMyPaper(HttpSession session) {
        if(!SessionUtil.existSession(session)) {
            return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
        }
        List<PaperDetailDTO> paperDetailDTOS = paperService.getAllPaperByCreatorId((Long) session.getAttribute("userId"));
        List<PaperDetailVO> paperDetailVO = paperDetailDTOS.stream()
                .map(DTOConvertToVOUtil::convertPaperDetailDTO)
                .collect(Collectors.toList());
        return new ResponseBody<>(ResponseBodyEnum.SUCCESS, paperDetailVO);
    }

    @PutMapping("/create")
    public ResponseBody<PaperDetailVO> createPaper(@RequestBody PaperCreateRequest paperCreateRequest, HttpSession session) {
        if(!paperCreateRequest.isComplete()) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
        }
        if(!SessionUtil.existSession(session)) {
            return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
        }
        PaperDetailDTO paperDetailDTO = paperService.createPaper(paperCreateRequest, (Long) session.getAttribute("userId"));
        PaperDetailVO paperDetailVO = DTOConvertToVOUtil.convertPaperDetailDTO(paperDetailDTO);
        return new ResponseBody<>(ResponseBodyEnum.SUCCESS, paperDetailVO);
    }

    @PutMapping("/addPolymerizationProblem")
    public ResponseBody<PolymerizationProblemVO> addPolymerizationProblem(@RequestBody PolymerizationProblemAddRequest polymerizationProblemAddRequest,
                                                                          HttpSession session) {
        if(!polymerizationProblemAddRequest.isComplete()) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
        }
        if(!SessionUtil.existSession(session)) {
            return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
        }
        Long userId = (Long) session.getAttribute("userId");
        PolymerizationProblemDTO polymerizationProblemDTO =
                paperService.addPolymerizationProblemInPaper(userId, polymerizationProblemAddRequest);
        if(polymerizationProblemDTO == null) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_NOT_MATCH);
        }
        PolymerizationProblemVO polymerizationProblemVO =
                DTOConvertToVOUtil.convertPolymerizationProblemDTO(polymerizationProblemDTO);
        return new ResponseBody<>(ResponseBodyEnum.SUCCESS, polymerizationProblemVO);
    }

    @PutMapping("/addImageInPolymerizationProblem")
    public ResponseBody<PolymerizationProblemVO> addImageInPolymerizationProblem(@RequestBody ImageInPolymerizationProblemAddRequest imageInPolymerizationProblemAddRequest,
                                                          HttpSession session) {
        if(!imageInPolymerizationProblemAddRequest.isComplete()) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
        }
        if(!SessionUtil.existSession(session)) {
            return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
        }
        Long userId = (Long) session.getAttribute("userId");
        PolymerizationProblemDTO polymerizationProblemDTO =
                paperService.addImageInPolymerizationProblem(userId, imageInPolymerizationProblemAddRequest);
        if(polymerizationProblemDTO == null) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_NOT_MATCH);
        }
        PolymerizationProblemVO polymerizationProblemVO =
                DTOConvertToVOUtil.convertPolymerizationProblemDTO(polymerizationProblemDTO);
        return new ResponseBody<>(ResponseBodyEnum.SUCCESS, polymerizationProblemVO);
    }

    @DeleteMapping("/deleteImageInPolymerizationProblem")
    public ResponseBody<PolymerizationProblemVO> deleteImageInPolymerizationProblem(@RequestBody ImageInPolymerizationProblemDeleteRequest imageInPolymerizationProblemDeleteRequest,
                                                        HttpSession session) {
        if(!imageInPolymerizationProblemDeleteRequest.isComplete()) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
        }
        if(!SessionUtil.existSession(session)) {
            return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
        }
        Long userId = (Long) session.getAttribute("userId");
        PolymerizationProblemDTO polymerizationProblemDTO =
                paperService.deleteImageInPolymerizationProblem(userId, imageInPolymerizationProblemDeleteRequest);
        if(polymerizationProblemDTO == null) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_NOT_MATCH);
        }
        PolymerizationProblemVO polymerizationProblemVO = DTOConvertToVOUtil.convertPolymerizationProblemDTO(polymerizationProblemDTO);
        return new ResponseBody<>(ResponseBodyEnum.SUCCESS, polymerizationProblemVO);
    }

    @PutMapping("/addProblem")
    public ResponseBody<ProblemVO> addProblem(@RequestBody ProblemAddRequest problemAddRequest, HttpSession session) {
        if(!problemAddRequest.isComplete()) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
        }
        if(!SessionUtil.existSession(session)) {
            return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
        }
        Long userId = (Long) session.getAttribute("userId");
        ProblemDTO problemDTO = paperService.addProblem(userId, problemAddRequest);
        if(problemDTO == null) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_NOT_MATCH);
        }
        ProblemVO problemVO = DTOConvertToVOUtil.convertProblemDTO(problemDTO);
        return new ResponseBody<>(ResponseBodyEnum.SUCCESS, problemVO);
    }

    @PutMapping("/addImageInProblem")
    public ResponseBody<ProblemVO> addImageInProblem(@RequestBody ImageInProblemAddRequest imageInProblemAddRequest,
                                                                                 HttpSession session) {
        if(!imageInProblemAddRequest.isComplete()) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
        }
        if(!SessionUtil.existSession(session)) {
            return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
        }
        Long userId = (Long) session.getAttribute("userId");
        ProblemDTO problemDTO = paperService.addImageInProblem(userId, imageInProblemAddRequest);
        if(problemDTO == null) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_NOT_MATCH);
        }
        ProblemVO problemVO = DTOConvertToVOUtil.convertProblemDTO(problemDTO);
        return new ResponseBody<>(ResponseBodyEnum.SUCCESS, problemVO);
    }

    @DeleteMapping("/deleteImageInProblem")
    public ResponseBody<ProblemVO> deleteImageInProblem(@RequestBody ImageInProblemDeleteRequest imageInProblemDeleteRequest,
                                               HttpSession session) {
        if(!imageInProblemDeleteRequest.isComplete()) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
        }
        if(!SessionUtil.existSession(session)) {
            return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
        }
        Long userId = (Long) session.getAttribute("userId");
        ProblemDTO problemDTO = paperService.deleteImageInProblem(userId, imageInProblemDeleteRequest);
        if(problemDTO == null) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_NOT_MATCH);
        }
        ProblemVO problemVO = DTOConvertToVOUtil.convertProblemDTO(problemDTO);
        return new ResponseBody<>(ResponseBodyEnum.SUCCESS, problemVO);
    }

    @DeleteMapping("/deleteProblem")
    public ResponseBody<PaperDetailVO> deleteProblem(ProblemDeleteRequest problemDeleteRequest,
                                            HttpSession session) {
        if(!problemDeleteRequest.isComplete()) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
        }
        if(!SessionUtil.existSession(session)) {
            return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
        }
        Long userId = (Long) session.getAttribute("userId");
        PaperDetailDTO paperDetailDTO = paperService.deleteProblem(userId, problemDeleteRequest);
        if(paperDetailDTO == null) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_NOT_MATCH);
        }
        PaperDetailVO paperDetailVO = DTOConvertToVOUtil.convertPaperDetailDTO(paperDetailDTO);
        return new ResponseBody<>(ResponseBodyEnum.SUCCESS, paperDetailVO);
    }

    @DeleteMapping("/deletePolymerizationProblem")
    public ResponseBody<PaperDetailVO> deletePolymerizationProblem(PolymerizationProblemDeleteRequest polymerizationProblemDeleteRequest,
                                                HttpSession session) {
        if(!polymerizationProblemDeleteRequest.isComplete()) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
        }
        if(!SessionUtil.existSession(session)) {
            return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
        }
        Long userId = (Long) session.getAttribute("userId");
        PaperDetailDTO paperDetailDTO = paperService.deletePolymerizationProblem(userId, polymerizationProblemDeleteRequest);
        if(paperDetailDTO == null) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_NOT_MATCH);
        }
        PaperDetailVO paperDetailVO = DTOConvertToVOUtil.convertPaperDetailDTO(paperDetailDTO);
        return new ResponseBody<>(ResponseBodyEnum.SUCCESS, paperDetailVO);
    }

    @DeleteMapping("/delete")
    public ResponseBody<Void> deletePaper(Long paperId, HttpSession session) {
        if(paperId == null) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
        }
        if(!SessionUtil.existSession(session)) {
            return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
        }
        Long userId = (Long) session.getAttribute("userId");
        boolean deletePaper = paperService.deletePaper(userId, paperId);
        if(!deletePaper) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_NOT_MATCH);
        }
        return new ResponseBody<>(ResponseBodyEnum.SUCCESS);
    }

    @PostMapping("/resortProblemsInPaper")
    public ResponseBody<PaperDetailVO> resortProblemsInPaper(@RequestBody ProblemsInPaperResortRequest problemsInPaperResortRequest,
                                                             HttpSession session) {
        if(!problemsInPaperResortRequest.isComplete()) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
        }
        if(!SessionUtil.existSession(session)) {
            return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
        }
        PaperDetailDTO paperDetailDTO =
                paperService.resortProblemsInPaper((Long) session.getAttribute("userId"), problemsInPaperResortRequest);
        if(paperDetailDTO == null) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_NOT_MATCH);
        }
        PaperDetailVO paperDetailVO = DTOConvertToVOUtil.convertPaperDetailDTO(paperDetailDTO);
        return new ResponseBody<>(ResponseBodyEnum.SUCCESS, paperDetailVO);
    }

    @PostMapping("/changePaperState")
    public ResponseBody<PaperVO> changePaperState(@RequestBody PaperStateChangeRequest paperStateChangeRequest,
                                                  HttpSession session) {
        if(!paperStateChangeRequest.isComplete()) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
        }
        if(!SessionUtil.existSession(session)) {
            return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
        }
        PaperDTO paperDTO =
                paperService.changePaperState((Long) session.getAttribute("userId"), paperStateChangeRequest);
        PaperVO paperVO = DTOConvertToVOUtil.convertPaperDTO(paperDTO);
        return new ResponseBody<>(ResponseBodyEnum.SUCCESS, paperVO);
    }
}
