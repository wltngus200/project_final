package com.green.fefu.Subjcet;

import com.green.fefu.Subjcet.model.SubjectReq;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SubjectMapper {
    int SubjectReq(SubjectReq p);

}
