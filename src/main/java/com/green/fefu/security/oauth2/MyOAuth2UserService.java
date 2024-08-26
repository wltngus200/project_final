package com.green.fefu.security.oauth2;

import com.green.fefu.entity.ParentOAuth2;
import com.green.fefu.entity.Parents;
import com.green.fefu.exception.CustomException;
import com.green.fefu.parents.ParentsUserMapper;
import com.green.fefu.parents.model.*;
import com.green.fefu.parents.repository.ParentOAuth2Repository;
import com.green.fefu.parents.repository.ParentRepository;
import com.green.fefu.security.MyUserDetails;
import com.green.fefu.security.MyUserOAuth2Vo;
import com.green.fefu.security.SignInProviderType;
import com.green.fefu.security.oauth2.userinfo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.green.fefu.exception.ljm.LjmErrorCode.*;


/*
    MyOAuth2UserService:
    OAuth2 제공자(구글, 페이스북, 카카오, 네이버 등)로부터 Access-Token 받은 후 loadUser메소드가 호출이 됩니다.
    ( 스프링 시큐리티에 구현되어 있음 )
    OAuth2 제공자로부터 사용자 정보를 가져온다. (이미 구현되어 있음. super.loadUser(userRequest);)
    OAuth2User 인터페이스를 구현한 객체(인증 객체)를 리턴

    FE > 플랫폼 소셜로그인 아이콘 클릭(리다이렉트 정보 전달 -- 이 리다이렉트는 로그인 완료 후 다시 돌아올 FE 주소값)
       > 백엔드에 요청('나 무슨 소셜로그인 하고 싶어'에 대한 정보가 전달)
       > 백엔드는 리다이렉트 (OAuth2 제공자 로그인 화면)
       > 해당 제공자의 아이디/비번을 작성 후 로그인 처리
       > 제공자는 인가 코드를 백엔드에게 보내준다.
       > (해당) 백엔드는 인가 코드를 가지고 access-token을 발급받는다.
       > (해당) access-token으로 사용자 정보(scope에 작성한 내용)를 받는다.
       > 이후는 자체 로그인 처리
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MyOAuth2UserService extends DefaultOAuth2UserService {
    private final ParentsUserMapper mapper ;
    private final OAuth2UserInfoFactory oAuth2UserInfoFactory ;
    private final ParentRepository parentRepository ;
    private final ParentOAuth2Repository oAuth2Repository ;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            return this.process(userRequest);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalAuthenticationServiceException(e.getMessage(), e.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest)  {
        OAuth2User oAuth2User = super.loadUser(userRequest); //제공자로부터 사용자정보를 얻음
        //각 소셜플랫폼에 맞는 enum타입을 얻는다.
        SignInProviderType signInProviderType = SignInProviderType.valueOf(userRequest.getClientRegistration()
                                                                                      .getRegistrationId()
                                                                                      .toUpperCase()
        );

        //규격화된 UserInfo객체로 변환
        // oAuth2User.getAttributes() > Data가 HashMap 객체로 변환
        OAuth2UserInfo oAuth2UserInfo = oAuth2UserInfoFactory.getOAuth2UserInfo(signInProviderType, oAuth2User.getAttributes());
        if(oAuth2UserInfo instanceof KakaoOAuth2UserInfo) {
            ((KakaoOAuth2UserInfo) oAuth2UserInfo).asd();
        }
        if(oAuth2UserInfo instanceof NaverOAuth2UserInfo) {
            ((NaverOAuth2UserInfo) oAuth2UserInfo).asd();
        }
        if(oAuth2UserInfo instanceof GoogleOAuth2UserInfo) {
            ((GoogleOAuth2UserInfo) oAuth2UserInfo).asd();
        }

        //기존에 회원가입이 되어있는가 체크
        SignInPostReq signInParam = new SignInPostReq();
        signInParam.setUid(oAuth2UserInfo.getId()); //플랫폼에서 넘어오는 유니크값(항상 같은 값이며 다른 사용자와 구별되는 유니크 값)
        signInParam.setProviderType(signInProviderType);
        List<ParentsUser> list = mapper.getParentUser(signInParam.getUid()) ;

        if( !list.isEmpty() && list.get(0).getAccept() != 1){
            throw new CustomException(YET_OK_USER) ;
        }
//        String phone = service.postSocialPhoneNumber() ;
        // 연동된 아이디가 없을 시
        if(list.isEmpty()) {
            Parents parents1 = new Parents() ;
            parents1.setAuth("ROLE_PARENTS") ;
            parents1.setEmail(oAuth2UserInfo.getEmail()) ;
            parents1.setName(oAuth2UserInfo.getName()) ;
            parents1.setUid(oAuth2UserInfo.getId()) ;
            parents1.setPhone("010-0000-0000") ;
            parents1.setConnect("기타") ;

            parentRepository.save(parents1) ;

            ParentOAuth2 oAuth2 = new ParentOAuth2() ;
            oAuth2.setId(oAuth2UserInfo.getId()) ;
            oAuth2.setName(oAuth2UserInfo.getName()) ;
            oAuth2.setEmail(oAuth2UserInfo.getEmail()) ;
            oAuth2.setProviderType(signInProviderType) ;
            oAuth2.setParentsId(parents1) ;
            oAuth2Repository.save(oAuth2) ;

            log.info("user: {}",parents1) ;
            System.out.println(parents1.getParentsId()) ;

            MyUserOAuth2Vo myUserOAuth2Vo =
                    new MyUserOAuth2Vo(parents1.getParentsId(), parents1.getAuth(), parents1.getName(), null) ;
            MyUserDetails signInUser = new MyUserDetails() ;
            signInUser.setMyUser(myUserOAuth2Vo); ;
            return signInUser ; // authentication 에 저장됨.
        }

        MyUserOAuth2Vo myUserOAuth2Vo =
                new MyUserOAuth2Vo(list.get(0).getParentsId(), list.get(0).getAuth(), list.get(0).getNm(), null) ;
        MyUserDetails signInUser = new MyUserDetails() ;
        signInUser.setMyUser(myUserOAuth2Vo); ;
        return signInUser ;
    }

}
