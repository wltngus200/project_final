package com.green.fefu.parents.utils;

import com.green.fefu.entity.Parents;
import com.green.fefu.parents.model.ParentsUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ParentUtils {
    public static Parents convertToUserInfo(List<ParentsUser> list){
        if(list == null || list.isEmpty()) {
            return null ;
        }
        Parents userInfoRoles = new Parents() ;
        ParentsUser userInfo = list.get(0) ;

        userInfoRoles.setParentsId(userInfo.getParentsId()); ;
        userInfoRoles.setUid(userInfo.getUid()) ;
        userInfoRoles.setUpw(userInfo.getUpw()) ;
        userInfoRoles.setName(userInfo.getNm());
        userInfoRoles.setCreatedAt(LocalDateTime.parse(userInfo.getCreatedAt())) ;
        userInfoRoles.setUpdatedAt(LocalDateTime.parse(userInfo.getUpdatedAt())) ;
        userInfoRoles.setAuth(userInfo.getAuth()); ;

        return userInfoRoles ;
    }
}
