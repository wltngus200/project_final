package com.green.fefu.online.repository;

import com.green.fefu.entity.HaesolOnlineMultiple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HaesolOnlineMultipleRepository extends JpaRepository<HaesolOnlineMultiple, Long> {
    @Query("SELECT hom from HaesolOnlineMultiple hom WHERE hom.haesolOnline.queId =:queId")
    List<HaesolOnlineMultiple> findSentenceByQueIdOrderByNum(@Param("queId")Long queId);

}
