package com.green.fefu.admin.test;

import com.green.fefu.admin.model.req.FindUnAcceptListReq;
import com.green.fefu.admin.model.req.adminUserReq;

import java.util.Map;

public interface AdminService {
    Map<String,Object> findUnAcceptList(FindUnAcceptListReq p, Map<String,Object> map);
    void deleteUser(adminUserReq p) ;
    void acceptUser(adminUserReq p) ;


}
