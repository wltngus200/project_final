package com.green.fefu.Subjcet;

import com.green.fefu.Subjcet.model.SubjectReq;
import com.green.fefu.common.ResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.green.fefu.common.GlobalConst.ERROR_CODE;
import static com.green.fefu.common.GlobalConst.SUCCESS_CODE;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/Subject")
public class SubjectControllerImpl implements SubjectController {
    private final SubjectService service;

    @PostMapping
    public ResultDto<Long> postSubject(SubjectReq p) {
        long res = service.postSubject(p);
        try {
            return ResultDto.resultDto(SUCCESS_CODE,"성적 기입성공",res);
        }catch (RuntimeException e) {
            return ResultDto.resultDto1(ERROR_CODE, "알수없는 오류");
        }
    }
}
