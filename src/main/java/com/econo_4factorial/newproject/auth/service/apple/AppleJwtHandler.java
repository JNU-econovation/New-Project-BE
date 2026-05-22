package com.econo_4factorial.newproject.auth.service.apple;

import com.econo_4factorial.newproject.auth.exception.BadRequestException.ExpiredTokenException;
import com.econo_4factorial.newproject.auth.exception.BadRequestException.SignatureException;
import com.econo_4factorial.newproject.auth.exception.InternalServerException.AppleTokenHeaderParsingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppleJwtHandler {
    public Map<String, String> parseHeaders(String token) {
        try {
            String header = token.split("\\.")[0]; //정규식을 활용해서 header만 추출
            return new ObjectMapper().readValue(decodeHeader(header), Map.class);
        } catch (JsonProcessingException | IllegalArgumentException e) {
            throw new AppleTokenHeaderParsingException();
        }
    }

    private String decodeHeader(String header) {
        return new String(
                Base64.getUrlDecoder().decode(header),
                StandardCharsets.UTF_8
        );
    }

    public Claims getTokenClaims(String token, PublicKey key) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (io.jsonwebtoken.security.SignatureException | MalformedJwtException e) {
            throw new SignatureException();
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException();
        }
    }
}
