package com.green.fefu.student.repository;

import com.green.fefu.entity.Parents;
import com.green.fefu.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByRandCode(String randCode);

    Student findStudentByGrade(int grade);
    Student findStudentByUid(String uid);

    Long countByParent(Parents parentsId);
    Long countByParentAndStateIs(Parents parentsId, Integer state);
    List<Student> findStudentsByParentOrderByGradeAsc(Parents parentsId);

    Optional<Student> findStudentsByRandCode(String randCode);
}
