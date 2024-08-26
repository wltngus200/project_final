package com.green.fefu.admin.test;

import com.green.fefu.admin.model.req.FindUnAcceptListReq;
import com.green.fefu.admin.model.req.adminUserReq;
import org.springframework.http.ResponseEntity;

public interface AdminController {
    ResponseEntity findUnAcceptList(FindUnAcceptListReq p);
    ResponseEntity deleteUser(adminUserReq p);
    ResponseEntity acceptUser(adminUserReq p);
}
