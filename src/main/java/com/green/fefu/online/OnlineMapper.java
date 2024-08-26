package com.green.fefu.online;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OnlineMapper {
    Long teacherClass(Long teaId);
    Long parentsClass(Long parentsId, Long stuId);
    Long studentClass(Long stuId);
    int tmpDeleteQuestion(long questionPk);
    int tmpDeleteQuestionMul(long questionPk);

}
