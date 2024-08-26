package com.green.fefu.calender.repository;

import com.green.fefu.entity.Calender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CalenderRepository extends JpaRepository<Calender, Long> {
    @Query("select ca from Calender ca order by ca.schedule asc ")
    List<Calender> findAllOrderBySchedule();
}
