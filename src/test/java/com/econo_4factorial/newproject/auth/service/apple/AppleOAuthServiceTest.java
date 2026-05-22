package com.econo_4factorial.newproject.auth.service.apple;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.econo_4factorial.newproject.auth.dto.Req.AppleLoginReq;
import com.econo_4factorial.newproject.auth.dto.Req.FullName;
import com.econo_4factorial.newproject.auth.dto.apple.ApplePublicKeysResponse;
import com.econo_4factorial.newproject.auth.dto.apple.AppleUserInfoDTO;
import com.econo_4factorial.newproject.auth.exception.BadRequestException.InvalidAudienceException;
import com.econo_4factorial.newproject.auth.exception.InternalServerException.NotAppleIssuerException;
import io.jsonwebtoken.Claims;
import java.security.PublicKey;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class AppleOAuthServiceTest {

    @Mock
    private AppleOAuthFeignClient appleOAuthFeignClient;

    @Mock
    private ApplePublicKeyGenerator applePublicKeyGenerator;

    @Mock
    private AppleJwtHandler appleJwtHandler;

    @Mock
    private PublicKey publicKey;

    @Mock
    private Claims claims;

    private AppleOAuthService appleOAuthService;

    @BeforeEach
    void setUp() {
        appleOAuthService = new AppleOAuthService(appleOAuthFeignClient, applePublicKeyGenerator, appleJwtHandler);
        ReflectionTestUtils.setField(appleOAuthService, "iss", "https://appleid.apple.com");
        ReflectionTestUtils.setField(appleOAuthService, "client_id", "com.sangyeol.app");
    }

    @Test
    void 애플_identityToken으로_유저정보를_조회한다() {
        AppleLoginReq request = new AppleLoginReq("identity-token", "test@example.com", new FullName("홍", "길동"));
        ApplePublicKeysResponse publicKeysResponse = new ApplePublicKeysResponse(java.util.List.of());
        given(appleJwtHandler.parseHeaders("identity-token")).willReturn(Map.of("kid", "kid-1", "alg", "RS256"));
        given(appleOAuthFeignClient.getApplePublicKeys()).willReturn(publicKeysResponse);
        given(applePublicKeyGenerator.generatePublicKey(publicKeysResponse,
                Map.of("kid", "kid-1", "alg", "RS256"))).willReturn(publicKey);
        given(appleJwtHandler.getTokenClaims("identity-token", publicKey)).willReturn(claims);
        given(claims.getIssuer()).willReturn("https://appleid.apple.com");
        given(claims.getAudience()).willReturn(Set.of("com.sangyeol.app"));
        given(claims.getSubject()).willReturn("apple-sub");

        AppleUserInfoDTO userInfo = appleOAuthService.getUserInfo(request);

        assertThat(userInfo.appleSub()).isEqualTo("apple-sub");
        assertThat(userInfo.name()).isEqualTo("홍길동");
        assertThat(userInfo.email()).isEqualTo("test@example.com");
        verify(appleJwtHandler).parseHeaders("identity-token");
        verify(appleOAuthFeignClient).getApplePublicKeys();
        verify(applePublicKeyGenerator).generatePublicKey(publicKeysResponse, Map.of("kid", "kid-1", "alg", "RS256"));
        verify(appleJwtHandler).getTokenClaims("identity-token", publicKey);
    }

    @Test
    void issuer가_애플이_아니면_예외가_발생한다() {
        AppleLoginReq request = new AppleLoginReq("identity-token", "test@example.com", new FullName("홍", "길동"));
        ApplePublicKeysResponse publicKeysResponse = new ApplePublicKeysResponse(java.util.List.of());
        given(appleJwtHandler.parseHeaders("identity-token")).willReturn(Map.of("kid", "kid-1", "alg", "RS256"));
        given(appleOAuthFeignClient.getApplePublicKeys()).willReturn(publicKeysResponse);
        given(applePublicKeyGenerator.generatePublicKey(publicKeysResponse,
                Map.of("kid", "kid-1", "alg", "RS256"))).willReturn(publicKey);
        given(appleJwtHandler.getTokenClaims("identity-token", publicKey)).willReturn(claims);
        given(claims.getIssuer()).willReturn("https://not-apple.com");

        assertThatThrownBy(() -> appleOAuthService.getUserInfo(request))
                .isInstanceOf(NotAppleIssuerException.class);
    }

    @Test
    void audience에_우리_client_id가_없으면_예외가_발생한다() {
        AppleLoginReq request = new AppleLoginReq("identity-token", "test@example.com", new FullName("홍", "길동"));
        ApplePublicKeysResponse publicKeysResponse = new ApplePublicKeysResponse(java.util.List.of());
        given(appleJwtHandler.parseHeaders("identity-token")).willReturn(Map.of("kid", "kid-1", "alg", "RS256"));
        given(appleOAuthFeignClient.getApplePublicKeys()).willReturn(publicKeysResponse);
        given(applePublicKeyGenerator.generatePublicKey(publicKeysResponse,
                Map.of("kid", "kid-1", "alg", "RS256"))).willReturn(publicKey);
        given(appleJwtHandler.getTokenClaims("identity-token", publicKey)).willReturn(claims);
        given(claims.getIssuer()).willReturn("https://appleid.apple.com");
        given(claims.getAudience()).willReturn(Set.of("another-client"));

        assertThatThrownBy(() -> appleOAuthService.getUserInfo(request))
                .isInstanceOf(InvalidAudienceException.class);
    }

    @Test
    void audience가_없으면_예외가_발생한다() {
        AppleLoginReq request = new AppleLoginReq("identity-token", "test@example.com", new FullName("홍", "길동"));
        ApplePublicKeysResponse publicKeysResponse = new ApplePublicKeysResponse(java.util.List.of());
        given(appleJwtHandler.parseHeaders("identity-token")).willReturn(Map.of("kid", "kid-1", "alg", "RS256"));
        given(appleOAuthFeignClient.getApplePublicKeys()).willReturn(publicKeysResponse);
        given(applePublicKeyGenerator.generatePublicKey(publicKeysResponse,
                Map.of("kid", "kid-1", "alg", "RS256"))).willReturn(publicKey);
        given(appleJwtHandler.getTokenClaims("identity-token", publicKey)).willReturn(claims);
        given(claims.getIssuer()).willReturn("https://appleid.apple.com");
        given(claims.getAudience()).willReturn(null);

        assertThatThrownBy(() -> appleOAuthService.getUserInfo(request))
                .isInstanceOf(InvalidAudienceException.class);
    }
}
