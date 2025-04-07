package com.econo_4factorial.newproject.auth.jwt.repository;

import com.econo_4factorial.newproject.auth.jwt.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
}
