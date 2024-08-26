package com.green.fefu.parents.repository;

import com.green.fefu.entity.ParentOAuth2;
import com.green.fefu.security.SignInProviderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ParentOAuth2Repository extends JpaRepository<ParentOAuth2, Long> {
    // 소셜 로그인
    ParentOAuth2 getParentsByProviderTypeAndId(SignInProviderType providerType, String uid) ;
    // 일반 로그인
    @Query(value = "select ff from Parents ff join ParentOAuth2 po on ff.parentsId = po.parentsId.parentsId where po.providerType = :providerType and po.parentsId = :parentId")
    ParentOAuth2 getParentsByProviderTypeAndParentId(SignInProviderType providerType, Long parentId) ;
}
