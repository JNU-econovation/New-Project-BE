package com.econo_4factorial.newproject.auth.service;

import com.econo_4factorial.newproject.auth.dto.Res.LoginRes;
import com.econo_4factorial.newproject.auth.service.kakao.KaKaoOAuthService;
import com.econo_4factorial.newproject.user.domain.User;
import com.econo_4factorial.newproject.user.dto.UserInfoDTO;
import com.econo_4factorial.newproject.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class OAuthService {
    private final UserService userService;
    private final KaKaoOAuthService kaKaoOAuthService;

    public String getKakaoLoginURI () {
        return kaKaoOAuthService.getLoginURI();
    }

    @Transactional
    public LoginRes loginWithKaKao (String kakaoAuthorizationCode) {
        UserInfoDTO userInfo = kaKaoOAuthService.getUserInfo(kakaoAuthorizationCode);
        User loginUser = userService.findOrCreateUserByUserInfo(userInfo);
        // jwt 토큰 발급 코드
        //발급된 jwt 토큰 redis에 저장 코드
        return new LoginRes("temp", "temp"); //추후 수정 필요
    }

}
