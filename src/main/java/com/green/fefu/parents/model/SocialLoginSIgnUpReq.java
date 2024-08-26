package com.green.fefu.parents.model;

import com.green.fefu.security.SignInProviderType;
import lombok.Data;

@Data
public class SocialLoginSIgnUpReq {
    private String name ;
    private String email ;
    private String id ;
    private SignInProviderType signInProviderType ;

    private String phone ;
    private String connect ;
    private String randomCode ;
}
