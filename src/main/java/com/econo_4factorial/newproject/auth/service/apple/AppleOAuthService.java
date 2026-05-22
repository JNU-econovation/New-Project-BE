package com.econo_4factorial.newproject.auth.service.apple;

import com.econo_4factorial.newproject.auth.dto.Req.AppleLoginReq;
import com.econo_4factorial.newproject.auth.dto.apple.ApplePublicKeysResponse;
import com.econo_4factorial.newproject.auth.dto.apple.AppleUserInfoDTO;
import com.econo_4factorial.newproject.auth.exception.BadRequestException.InvalidAudienceException;
import com.econo_4factorial.newproject.auth.exception.InternalServerException.NotAppleIssuerException;
import io.jsonwebtoken.Claims;
import java.security.PublicKey;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AppleOAuthService {
    private final AppleOAuthFeignClient appleOAuthFeignClient;
    private final ApplePublicKeyGenerator applePublicKeyGenerator;
    private final AppleJwtHandler AppleJwtHandler;

    @Value("${oauth.apple.iss}")
    private String iss;

    @Value("${oauth.apple.client-id}")
    private String client_id;

    public AppleUserInfoDTO getUserInfo(AppleLoginReq appleLoginReq) {
        Map<String, String> headers = AppleJwtHandler.parseHeaders(appleLoginReq.identityToken()); //identityToken 헤더 파싱
        ApplePublicKeysResponse applePublicKeys = appleOAuthFeignClient.getApplePublicKeys(); //애플 공개키 데이터 가져오기
        PublicKey publicKey = applePublicKeyGenerator.generatePublicKey(applePublicKeys,
                headers); //identityToken 서명 검증에서 쓰이는 공개키 생성
        Claims tokenClaims = AppleJwtHandler.getTokenClaims(appleLoginReq.identityToken(),
                publicKey); // 토큰에서 들어있는 claim 값 가져오기
        validateClaims(tokenClaims); // identityToken 검증
        return appleLoginReq.toAppleUserInfoDTO(tokenClaims.getSubject());
    }

    private void validateClaims(Claims tokenClaims) {
        isAppleIssuer(tokenClaims);
        isOurServiceAudience(tokenClaims);
    }

    private void isAppleIssuer(Claims tokenClaims) {
        if (!iss.equals(tokenClaims.getIssuer())) {
            throw new NotAppleIssuerException();
        }
    }

    private void isOurServiceAudience(Claims tokenClaims) {
        if (tokenClaims.getAudience() == null || !tokenClaims.getAudience().contains(client_id)) {
            throw new InvalidAudienceException();
        }
    }


}
