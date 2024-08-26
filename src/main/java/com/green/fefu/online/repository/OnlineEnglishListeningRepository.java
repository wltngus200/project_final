package com.green.fefu.online.repository;

import com.green.fefu.entity.OnlineEnglishListening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OnlineEnglishListeningRepository extends JpaRepository<OnlineEnglishListening, Long> {
    List<OnlineEnglishListening> getAllByGrade(@Param("grade") long grade);
}
