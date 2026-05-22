package com.econo_4factorial.newproject.auth.service.apple;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.econo_4factorial.newproject.auth.dto.apple.ApplePublicKeysResponse;
import com.econo_4factorial.newproject.auth.exception.InternalServerException.ApplePublicKeyGenerateException;
import com.econo_4factorial.newproject.auth.exception.InternalServerException.NotMatchedApplePublicKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApplePublicKeyGeneratorTest {

    private ApplePublicKeyGenerator applePublicKeyGenerator;

    @BeforeEach
    void setUp() {
        applePublicKeyGenerator = new ApplePublicKeyGenerator();
    }

    @Test
    void 헤더와_일치하는_애플_공개키를_생성한다() throws Exception {
        KeyPair keyPair = createKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        ApplePublicKeysResponse response = new ApplePublicKeysResponse(List.of(
                new ApplePublicKeysResponse.ApplePublicKey(
                        "RSA",
                        "kid-1",
                        "sig",
                        "RS256",
                        encode(publicKey.getModulus().toByteArray()),
                        encode(publicKey.getPublicExponent().toByteArray())
                )
        ));

        PublicKey generatedKey = applePublicKeyGenerator.generatePublicKey(response,
                Map.of("kid", "kid-1", "alg", "RS256"));

        assertThat(generatedKey.getEncoded()).isEqualTo(publicKey.getEncoded());
    }

    @Test
    void 매칭되는_키가_없으면_예외가_발생한다() throws Exception {
        KeyPair keyPair = createKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        ApplePublicKeysResponse response = new ApplePublicKeysResponse(List.of(
                new ApplePublicKeysResponse.ApplePublicKey(
                        "RSA",
                        "kid-1",
                        "sig",
                        "RS256",
                        encode(publicKey.getModulus().toByteArray()),
                        encode(publicKey.getPublicExponent().toByteArray())
                )
        ));

        assertThatThrownBy(
                () -> applePublicKeyGenerator.generatePublicKey(response, Map.of("kid", "kid-2", "alg", "RS256")))
                .isInstanceOf(NotMatchedApplePublicKeyException.class);
    }

    @Test
    void 공개키_생성에_실패하면_예외가_발생한다() throws Exception {
        KeyPair keyPair = createKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        ApplePublicKeysResponse response = new ApplePublicKeysResponse(List.of(
                new ApplePublicKeysResponse.ApplePublicKey(
                        "INVALID",
                        "kid-1",
                        "sig",
                        "RS256",
                        encode(publicKey.getModulus().toByteArray()),
                        encode(publicKey.getPublicExponent().toByteArray())
                )
        ));

        assertThatThrownBy(
                () -> applePublicKeyGenerator.generatePublicKey(response, Map.of("kid", "kid-1", "alg", "RS256")))
                .isInstanceOf(ApplePublicKeyGenerateException.class);
    }

    private KeyPair createKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    private String encode(byte[] value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(stripLeadingZero(value));
    }

    private byte[] stripLeadingZero(byte[] value) {
        if (value.length > 1 && value[0] == 0) {
            byte[] stripped = new byte[value.length - 1];
            System.arraycopy(value, 1, stripped, 0, stripped.length);
            return stripped;
        }
        return value;
    }
}
