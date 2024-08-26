package com.green.fefu.semester;

import com.green.fefu.common.ResultDto;
import com.green.fefu.semester.model.SemesterReq;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;


public interface SemesterController {
     ResultDto<Integer> postSemester(SemesterReq p);
}
