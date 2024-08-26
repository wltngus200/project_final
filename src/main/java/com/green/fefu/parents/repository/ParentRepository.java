package com.green.fefu.parents.repository;

import com.green.fefu.entity.Parents;
import com.green.fefu.security.SignInProviderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParentRepository extends JpaRepository<Parents, Long> {
    @Query(value = "select ff from Parents ff " +
            "inner join ParentOAuth2 po2 " +
            "on ff.parentsId = po2.parentsId.parentsId " +
            "where po2.providerType = :providerType" +
            " and po2.parentsId.parentsId = :parentsId")
    Parents getParentsByProviderTypeAndParentsId(SignInProviderType providerType, long parentsId) ;

    @Query(value = "select ff.parentsId from Parents ff where ff.parentsId = :parentsId")
    long getParentsByParentsId(long parentsId) ;

    @Query(value = "select ff from Parents ff " +
            "inner join ParentOAuth2 po2 " +
            "on ff.parentsId = po2.parentsId.parentsId " +
            "where po2.providerType = :providerType" +
            " and po2.id = :id " +
            " and po2.parentsId.parentsId = :parentsId")
    Parents getParentsByProviderTypeAndUidAndParentsPk(SignInProviderType providerType, String id, long parentsId) ;

    Parents findParentsByPhoneAndUid(String phone, String uid) ;

    @Query("SELECT p FROM Parents p WHERE p.uid = :uid")
    Parents findParentByUid(String uid);

    List<Parents> findAllByStateIsAndAcceptIs(Integer state, Integer accept);

    List<Parents> findByNameContainingAndStateIsAndAcceptIs(String name, Integer state, Integer accept);
}
