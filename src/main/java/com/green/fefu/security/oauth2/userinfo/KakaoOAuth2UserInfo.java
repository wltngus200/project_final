package com.green.fefu.security.oauth2.userinfo;

import lombok.ToString;

import java.util.Map;


@ToString
public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getName() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        return properties == null ? null : properties.get("nickname").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("account_email").toString();
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
