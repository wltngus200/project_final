package com.green.fefu.score;

import com.green.fefu.score.model.Dto;
import com.green.fefu.score.model.DtoDetail;
import com.green.fefu.score.model.GetDetailScoreReq;
import com.green.fefu.score.model.InsScoreReq;

public interface ScoreService {
    long postScore(InsScoreReq p);

    public Dto getScore(GetScoreReq p);

    public  DtoDetail getDetailScore(GetDetailScoreReq p);
}
