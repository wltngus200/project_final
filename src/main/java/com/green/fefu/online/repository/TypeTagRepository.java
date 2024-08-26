package com.green.fefu.online.repository;

import com.green.fefu.entity.dummy.TypeTag;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypeTagRepository extends JpaRepository<TypeTag, Long> {
    TypeTag findByTypeNum(Long typeNum);

}
