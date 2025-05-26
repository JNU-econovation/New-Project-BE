package com.econo_4factorial.newproject.auth.service.apple;

import com.econo_4factorial.newproject.auth.exception.BadRequestException.ExpiredTokenException;
import com.econo_4factorial.newproject.auth.exception.InternalServerException.AppleTokenHeaderParsingException;
import com.econo_4factorial.newproject.auth.exception.BadRequestException.InvalidTokenException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AppleJwtHandler {
    public Map<String, String> parseHeaders(String token) {
        try {
            String header = token.split("\\.")[0]; //정규식을 활용해서 header만 추출
            return new ObjectMapper().readValue(decodeHeader(header), Map.class);
        }catch (JsonProcessingException e) {
            throw new AppleTokenHeaderParsingException();
        }
    }

    private String decodeHeader(String header) {
        return new String(
                Base64.getDecoder().decode(header),
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
        } catch (SignatureException | MalformedJwtException e) {
            throw new InvalidTokenException();
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException();
        }
    }
}
