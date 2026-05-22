package com.econo_4factorial.newproject.auth.service.apple;

import com.econo_4factorial.newproject.auth.dto.apple.ApplePublicKeysResponse;
import com.econo_4factorial.newproject.auth.dto.apple.ApplePublicKeysResponse.ApplePublicKey;
import com.econo_4factorial.newproject.auth.exception.InternalServerException.ApplePublicKeyGenerateException;
import com.econo_4factorial.newproject.auth.exception.InternalServerException.NotMatchedApplePublicKeyException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplePublicKeyGenerator {
    public PublicKey generatePublicKey(ApplePublicKeysResponse applePublicKeys, Map<String, String> headers) {
        ApplePublicKey matchedKey = applePublicKeys.keys().stream()
                .filter(key -> key.kid().equals(headers.get("kid")) && key.alg().equals(headers.get("alg")))
                .findAny()
                .orElseThrow(() -> new NotMatchedApplePublicKeyException());
        return generateKey(matchedKey);
    }

    private PublicKey generateKey(ApplePublicKey publicKey) {
        try {
            byte[] nBytes = Base64.getUrlDecoder().decode(publicKey.n());
            byte[] eBytes = Base64.getUrlDecoder().decode(publicKey.e());
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(
                    new BigInteger(1, nBytes),
                    new BigInteger(1, eBytes)
            );
            KeyFactory keyFactory = KeyFactory.getInstance(publicKey.kty());
            return keyFactory.generatePublic(publicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new ApplePublicKeyGenerateException();
        }
    }


}
