package com.green.fefu.student.repository;

import com.green.fefu.entity.Class;
import com.green.fefu.entity.Student;
import com.green.fefu.entity.StudentClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudentClassRepository extends JpaRepository<StudentClass, Integer> {
    StudentClass findAllByStuId(Student stuId);

    Long countByClassId(Class classId);
    @Query("select sc from StudentClass sc where sc.classId.classId = :classId and sc.stuId.stuId = :stuId")
    StudentClass findByClassIdAndStuId(Integer classId, Long stuId);
    @Query("select sc from StudentClass sc where sc.classId.classId = :classId order by sc.scId desc limit 1")
    StudentClass findTopByClassIdOrderByScId(Integer classId);
}
