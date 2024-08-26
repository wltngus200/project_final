package com.green.fefu.Subjcet;

import com.green.fefu.Subjcet.model.SubjectReq;
import com.green.fefu.common.ResultDto;

public interface SubjectController {
    ResultDto<Long> postSubject(SubjectReq p);
}
