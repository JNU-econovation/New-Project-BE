package com.econo_4factorial.newproject.auth.dto.apple;

import java.util.List;

public record ApplePublicKeysResponse(
        List<ApplePublicKey> keys
) {
    public record ApplePublicKey(
            String kty,
            String kid,
            String use,
            String alg,
            String n,
            String e
    ) {
    }
}
