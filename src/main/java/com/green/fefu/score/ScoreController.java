package com.green.fefu.score;

import com.green.fefu.common.ResultDto;
import com.green.fefu.score.model.Dto;
import com.green.fefu.score.model.InsScoreReq;
import com.green.fefu.score.model.StuGetRes;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.ModelAttribute;

public interface ScoreController {
    public ResultDto<Long> postScore(InsScoreReq p);
    ResultDto<Dto> getScore( StuGetRes p);
}
