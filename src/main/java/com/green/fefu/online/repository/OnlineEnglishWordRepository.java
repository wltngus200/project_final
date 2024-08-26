package com.green.fefu.online.repository;

import com.green.fefu.entity.OnlineEnglishWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OnlineEnglishWordRepository extends JpaRepository<OnlineEnglishWord, Long> {
    List<OnlineEnglishWord> getAllByGrade(@Param("grade") long grade);
}
