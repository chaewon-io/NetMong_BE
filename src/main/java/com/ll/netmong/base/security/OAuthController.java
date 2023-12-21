package com.ll.netmong.base.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/oauth2")
public class OAuthController {

    @GetMapping("/code/null")
    public String oauthLoginInfo(Authentication authentication) {
        System.out.println("controller authentication = " + authentication);
        //oAuth2User.toString() 예시 : Name: [2346930276], Granted Authorities: [[USER]], User Attributes: [{id=2346930276, provider=kakao, name=김준우, email=bababoll@naver.com}]
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        //attributes.toString() 예시 : {id=2346930276, provider=kakao, name=김준우, email=bababoll@naver.com}

        System.out.println("oAuth2User = " + oAuth2User);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        return attributes.toString();
    }
}
