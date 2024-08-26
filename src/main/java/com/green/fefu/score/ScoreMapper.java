package com.green.fefu.score;

import com.green.fefu.score.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScoreMapper {
    int postScore(InsScoreReq p);

    List<InsScoreList> getScoreMidterm(StuGetRes p);

    List<InsScoreList> getScoreFinal(StuGetRes p);

    StuGetRes getStu(long stuId);

    List<InsScoreList> getDetailScore(GetDetailScoreReq p);

    List<InsScoreList> getDetailScoreFinal(GetDetailScoreReq p);

    int delScore(DelScore p);

    List<InsScoreList> totalList(DelScore p);

    RankRes rankListFinal(RankReq p);

   RankRes rankListMid(RankReq p);

   GetClassIdRes getClassId(long teaId, long stuId);

   SignResult signResult(SignResult p );

   int postScoreList(InsScoreReq p);

    //Tdd
    InsScoreList selScore(long StudentPk);


}
