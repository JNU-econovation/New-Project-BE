package com.econo_4factorial.newproject.auth.jwt.repository;

import com.econo_4factorial.newproject.auth.jwt.BlacklistToken;
import org.springframework.data.repository.CrudRepository;

public interface BlacklistTokenRepository extends CrudRepository<BlacklistToken, String> {
}
