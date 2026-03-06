package com.econo_4factorial.newproject.auth.jwt;

import org.springframework.data.annotation.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash(value = "blacklist")
public class BlacklistToken {

    @Id
    private String accessToken;

    private String status;

    @TimeToLive
    private Long expirationTime;
}
