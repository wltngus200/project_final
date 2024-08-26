package com.green.fefu.teacher.repository;

import com.green.fefu.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    @Query("select te from Teacher te where te.uid = :uid")
    Teacher findByUid(String uid);

    Teacher findByEmail(String email);

    Teacher findByNameAndPhone(String name, String phone);

    List<Teacher> findAllByStateIsAndAcceptIs(Integer state, Integer accept);

    List<Teacher> findByNameContainingAndStateIsAndAcceptIs(String name, Integer state, Integer accept);


}
