package com.green.fefu.online.repository;

import com.green.fefu.entity.dummy.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
