package com.green.fefu.student.repository;

import com.green.fefu.entity.Class;
import com.green.fefu.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClassRepository extends JpaRepository<Class,Integer> {
    @Query("select cl from Class cl where cl.teaId.teaId = :teaId")

    Class findByTeaId(Long teaId);

    Class findByTeaId(Teacher teaId);
}
