package com.green.fefu.security.oauth2.userinfo;

import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        Map<String, Object> response = (Map<String, Object>)attributes.get("response");
        return response == null ? null : response.get("id").toString();
    }

    @Override
    public String getName() {
        Map<String, Object> response = (Map<String, Object>)attributes.get("response");
        return response == null ? null : response.get("name").toString();
    }

    @Override
    public String getEmail() {
        Map<String, Object> response = (Map<String, Object>)attributes.get("response");
        return response == null ? null : response.get("email").toString();
    }

    @Override
    public String getProfilePicUrl() {
        return null ;
    }

    @Override
    public String getAuthorities(){
        return null ;
    }

    public void asd()
    {
        System.out.println("asd: " + this.attributes.toString());
    }
}
