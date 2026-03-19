package com.econo_4factorial.newproject.auth.service.apple;

import com.econo_4factorial.newproject.auth.exception.BadRequestException.ExpiredTokenException;
import com.econo_4factorial.newproject.auth.exception.BadRequestException.SignatureException;
import com.econo_4factorial.newproject.auth.exception.InternalServerException.AppleTokenHeaderParsingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AppleJwtHandlerTest {

    private AppleJwtHandler appleJwtHandler;

    @BeforeEach
    void setUp() {
        appleJwtHandler = new AppleJwtHandler();
    }

    @Test
    void 토큰_헤더를_파싱한다() {
        String headerJson = "{\"kid\":\"kid-1\",\"alg\":\"RS256\"}";
        String encodedHeader = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(headerJson.getBytes(StandardCharsets.UTF_8));

        Map<String, String> headers = appleJwtHandler.parseHeaders(encodedHeader + ".payload.signature");

        assertThat(headers.get("kid")).isEqualTo("kid-1");
        assertThat(headers.get("alg")).isEqualTo("RS256");
    }

    @Test
    void 잘못된_헤더_JSON이면_예외가_발생한다() {
        String encodedHeader = Base64.getUrlEncoder().withoutPadding()
                .encodeToString("invalid-json".getBytes(StandardCharsets.UTF_8));

        assertThatThrownBy(() -> appleJwtHandler.parseHeaders(encodedHeader + ".payload.signature"))
                .isInstanceOf(AppleTokenHeaderParsingException.class);
    }

    @Test
    void 잘못된_Base64URL_헤더면_예외가_발생한다() {
        assertThatThrownBy(() -> appleJwtHandler.parseHeaders("not_base64url!.payload.signature"))
                .isInstanceOf(AppleTokenHeaderParsingException.class);
    }

    @Test
    void 공개키로_토큰_클레임을_파싱한다() throws Exception {
        KeyPair keyPair = createKeyPair();
        String token = Jwts.builder()
                .claim("sub", "apple-user")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 60_000))
                .signWith(keyPair.getPrivate())
                .compact();

        Claims claims = appleJwtHandler.getTokenClaims(token, keyPair.getPublic());

        assertThat(claims.get("sub", String.class)).isEqualTo("apple-user");
    }

    @Test
    void 만료된_애플_토큰이면_예외가_발생한다() throws Exception {
        KeyPair keyPair = createKeyPair();
        String token = Jwts.builder()
                .claim("sub", "apple-user")
                .issuedAt(new Date(System.currentTimeMillis() - 10_000))
                .expiration(new Date(System.currentTimeMillis() - 5_000))
                .signWith(keyPair.getPrivate())
                .compact();

        assertThatThrownBy(() -> appleJwtHandler.getTokenClaims(token, keyPair.getPublic()))
                .isInstanceOf(ExpiredTokenException.class);
    }

    @Test
    void 공개키가_맞지않으면_서명예외가_발생한다() throws Exception {
        KeyPair signerKeyPair = createKeyPair();
        PublicKey wrongPublicKey = createKeyPair().getPublic();
        String token = Jwts.builder()
                .claim("sub", "apple-user")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 60_000))
                .signWith(signerKeyPair.getPrivate())
                .compact();

        assertThatThrownBy(() -> appleJwtHandler.getTokenClaims(token, wrongPublicKey))
                .isInstanceOf(SignatureException.class);
    }

    private KeyPair createKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }
}
