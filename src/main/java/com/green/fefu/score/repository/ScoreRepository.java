package com.green.fefu.score.repository;


import com.green.fefu.entity.Score;
import com.green.fefu.entity.StudentClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<Score, Long> {

}
