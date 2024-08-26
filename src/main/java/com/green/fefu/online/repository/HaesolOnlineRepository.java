package com.green.fefu.online.repository;


import com.green.fefu.entity.HaesolOnline;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HaesolOnlineRepository extends JpaRepository<HaesolOnline,Long> {
    // 선택한 과목과 학년에 맞는 문제 전부 출력
    @Query("SELECT ha FROM HaesolOnline ha WHERE ha.subjectCode.subjectId=:subjectCode AND ha.classId=:classId")
    List<HaesolOnline> findBySubjectCodeAndClassId(@Param("subjectCode")Long subjectCode, @Param("classId")Long classId);

    // PK값 리스트에 맞는 정답 리스트
    @Query("SELECT ha.answer FROM HaesolOnline ha WHERE ha.queId IN (:queId)")
    Integer findAnswerByQueId(@Param("que_id") Long queId);

    @Query(value = "SELECT B.tag_name\n" +
                   "FROM (SELECT que_id, type_tag\n" +
                         "FROM haesol_online\n" +
                         "WHERE que_id = :queId) A\n" +
                   "INNER JOIN type_tag B\n" +
                   "ON A.type_tag=B.tag_id;\n", nativeQuery = true)
    String findTypeNameByQueId(@Param("que_id") Long queId);


    @Query("SELECT ha FROM HaesolOnline ha WHERE ha.classId=:classId")
    List<HaesolOnline> findByClassId(@Param("classId")Long classId);
}
