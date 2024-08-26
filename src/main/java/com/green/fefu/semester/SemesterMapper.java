package com.green.fefu.semester;

import com.green.fefu.semester.model.SemesterReq;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SemesterMapper {
    int postSemester(SemesterReq p);


}
