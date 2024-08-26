package com.green.fefu.online.repository;

import com.green.fefu.entity.StudentOnlineRecode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentOnlineRecodeRepository extends JpaRepository<StudentOnlineRecode, Long> {
    List<StudentOnlineRecode> getByStuOnId(Long stuOnId);
}
