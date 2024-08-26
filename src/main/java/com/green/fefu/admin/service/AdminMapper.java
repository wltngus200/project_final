package com.green.fefu.admin.service;

import com.green.fefu.admin.model.dto.GetUserListDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {
    List<GetUserListDto> getParentList(String searchWord);

    List<GetUserListDto> getTeacherList(String searchWord);

    int delParent(long pk);

    int delTeacher(long pk);

    int updParent(long pk);

    int updTeacher(long pk);

    int delClassTeacher(long teaId) ;
}
