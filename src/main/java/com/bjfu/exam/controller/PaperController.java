package com.bjfu.exam.controller;

import com.bjfu.exam.dto.paper.PaperDTO;
import com.bjfu.exam.dto.paper.PaperDetailDTO;
import com.bjfu.exam.dto.paper.PolymerizationProblemDTO;
import com.bjfu.exam.dto.paper.ProblemDTO;
import com.bjfu.exam.enums.ResponseBodyEnum;
import com.bjfu.exam.request.*;
import com.bjfu.exam.service.PaperService;
import com.bjfu.exam.util.DTOConvertToVOUtil;
import com.bjfu.exam.vo.ResponseBody;
import com.bjfu.exam.vo.paper.PaperDetailVO;
import com.bjfu.exam.vo.paper.PaperVO;
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
    public ResponseBody<PaperVO> getPaper(String code) {
        if(StringUtils.isEmpty(code)) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
        }
        PaperDTO paperDTO = paperService.getPaper(code);
        if(paperDTO != null) {
            PaperVO paperVO = DTOConvertToVOUtil.convertPaperDTO(paperDTO);
            return new ResponseBody<>(ResponseBodyEnum.SUCCESS, paperVO);
        }
        return new ResponseBody<>(ResponseBodyEnum.FIND_FAILED);
    }

    @GetMapping("/get")
    public ResponseBody<List<PaperDetailVO>> getMyPaper(HttpSession session) {
        if(session.getAttribute("userId") == null) {
            return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
        }
        List<PaperDetailDTO> paperDetailDTOS = paperService.getAllPaperByCreatorId((Long) session.getAttribute("userId"));
        List<PaperDetailVO> paperDetailVO = paperDetailDTOS.stream()
                .map(DTOConvertToVOUtil::convertPaperDetailDTO)
                .collect(Collectors.toList());
        return new ResponseBody<>(ResponseBodyEnum.SUCCESS, paperDetailVO);
    }

    @PostMapping("/create")
    public ResponseBody<PaperVO> createPaper(@RequestBody PaperCreateRequest paperCreateRequest, HttpSession session) {
        if(paperCreateRequest.isComplete()) {
            if(session.getAttribute("userId") == null) {
                return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
            }
            PaperDTO paperDTO = paperService.createPaper(paperCreateRequest, (Long) session.getAttribute("userId"));
            if(paperDTO == null) {
                return new ResponseBody<>(ResponseBodyEnum.UNKNOWN_WRONG);
            }
            PaperVO paperVO = DTOConvertToVOUtil.convertPaperDTO(paperDTO);
            return new ResponseBody<>(ResponseBodyEnum.SUCCESS, paperVO);
        }
        return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
    }

    @DeleteMapping("/delete")
    public ResponseBody<Void> deletePaper(Long paperId, HttpSession session) {
        if(session.getAttribute("userId") == null) {
            return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
        }
        if(paperId == null) {
            return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
        }
        Long userId = (Long) session.getAttribute("userId");
        paperService.deletePaper(userId, paperId);
        return new ResponseBody<>(ResponseBodyEnum.SUCCESS);
    }

    @PutMapping("/addPolymerizationProblem")
    public ResponseBody<Void> addPolymerizationProblem(@RequestBody PolymerizationProblemAddRequest polymerizationProblemAddRequest,
                                                       HttpSession session) {
        if(session.getAttribute("userId") == null) {
            return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
        }
        if(polymerizationProblemAddRequest.isComplete()) {
            Long userId = (Long) session.getAttribute("userId");
            PolymerizationProblemDTO polymerizationProblemDTO =
                    paperService.addPolymerizationProblemInPaper(userId, polymerizationProblemAddRequest);
            if(polymerizationProblemDTO == null) {
                return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
            }
            return new ResponseBody<>(ResponseBodyEnum.SUCCESS);
        }
        return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
    }

    @PutMapping("/addProblem")
    public ResponseBody<Void> addProblem(@RequestBody ProblemAddRequest problemAddRequest, HttpSession session) {
        if(session.getAttribute("userId") == null) {
            return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
        }
        if(problemAddRequest.isComplete()) {
            Long userId = (Long) session.getAttribute("userId");
            ProblemDTO problemDTO = paperService.addProblem(userId, problemAddRequest);
            if(problemDTO == null) {
                return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
            }
            return new ResponseBody<>(ResponseBodyEnum.SUCCESS);
        }
        return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
    }

    @DeleteMapping("/deleteProblem")
    public ResponseBody<PaperDetailVO> deleteProblem(@RequestBody ProblemDeleteRequest problemDeleteRequest,
                                            HttpSession session) {
        if(session.getAttribute("userId") == null) {
            return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
        }
        if(problemDeleteRequest.isComplete()) {
            Long userId = (Long) session.getAttribute("userId");
            PaperDetailDTO paperDetailDTO = paperService.deleteProblem(userId, problemDeleteRequest);
            if(paperDetailDTO == null) {
                return new ResponseBody<>(ResponseBodyEnum.PARAM_NOT_MATCH);
            }
            PaperDetailVO paperDetailVO = DTOConvertToVOUtil.convertPaperDetailDTO(paperDetailDTO);
            return new ResponseBody<>(ResponseBodyEnum.SUCCESS, paperDetailVO);
        }
        return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
    }

    @DeleteMapping("/deletePolymerizationProblem")
    public ResponseBody<PaperDetailVO> deletePolymerizationProblem(@RequestBody PolymerizationProblemDeleteRequest polymerizationProblemDeleteRequest,
                                                HttpSession session) {
        if(session.getAttribute("userId") == null) {
            return new ResponseBody<>(ResponseBodyEnum.NEED_TO_RELOGIN);
        }
        if(polymerizationProblemDeleteRequest.isComplete()) {
            Long userId = (Long) session.getAttribute("userId");
            PaperDetailDTO paperDetailDTO = paperService.deletePolymerizationProblem(userId, polymerizationProblemDeleteRequest);
            if(paperDetailDTO == null) {
                return new ResponseBody<>(ResponseBodyEnum.PARAM_NOT_MATCH);
            }
            PaperDetailVO paperDetailVO = DTOConvertToVOUtil.convertPaperDetailDTO(paperDetailDTO);
            return new ResponseBody<>(ResponseBodyEnum.SUCCESS, paperDetailVO);
        }
        return new ResponseBody<>(ResponseBodyEnum.PARAM_WRONG);
    }
}
