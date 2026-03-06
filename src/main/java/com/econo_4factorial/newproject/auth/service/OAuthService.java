    package com.econo_4factorial.newproject.auth.service;

    import com.econo_4factorial.newproject.auth.dto.Req.AppleLoginReq;
    import com.econo_4factorial.newproject.auth.dto.apple.AppleUserInfoDTO;
    import com.econo_4factorial.newproject.auth.dto.kakao.KakaoUserInfoDTO;
    import com.econo_4factorial.newproject.auth.exception.BadRequestException.AuthException;
    import com.econo_4factorial.newproject.auth.jwt.AuthToken;
    import com.econo_4factorial.newproject.auth.jwt.service.AuthTokenService;
    import com.econo_4factorial.newproject.auth.service.apple.AppleOAuthService;
    import com.econo_4factorial.newproject.auth.service.kakao.KaKaoOAuthService;
    import com.econo_4factorial.newproject.common.exception.BadRequestException;
    import com.econo_4factorial.newproject.user.domain.User;
    import com.econo_4factorial.newproject.user.service.UserService;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    @Slf4j
    @Service
    @RequiredArgsConstructor
    public class OAuthService {
        private final UserService userService;
        private final KaKaoOAuthService kaKaoOAuthService;
        private final AppleOAuthService appleOAuthService;
        private final AuthTokenService authTokenService;

        public String getKakaoLoginURI () {
            return kaKaoOAuthService.getLoginURI();
        }

        @Transactional
        public AuthToken loginWithKaKao (String kakaoAuthorizationCode) {
            try {
                KakaoUserInfoDTO userInfo = kaKaoOAuthService.getUserInfo(kakaoAuthorizationCode);
                User loginUser = userService.findOrCreateUserByKakaoUserInfo(userInfo);
                return authTokenService.issueAuthToken(loginUser.getId());
            } catch (BadRequestException e) {
                throw new AuthException();
            }
        }

        @Transactional
        public AuthToken loginWithApple (AppleLoginReq appleLoginReq) {
            try {
                AppleUserInfoDTO userInfo =appleOAuthService.getUserInfo(appleLoginReq);
                log.info("애플 유저 정보: {}", userInfo);
                User loginUser = userService.findOrCreateUserByAppleUserInfo(userInfo);
                log.info("생성된 유저 정보: {}", loginUser.getId());
                return authTokenService.issueAuthToken(loginUser.getId());
            } catch (BadRequestException e) {
                throw new AuthException();
            }
        }

        public void logout(Long userId, String accessToken) {
            authTokenService.logout(userId, accessToken);
        }

        public AuthToken reissue(String refreshToken) {
            return authTokenService.reissue(refreshToken);
        }
    }
