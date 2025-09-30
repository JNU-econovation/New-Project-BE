package com.econo_4factorial.newproject.user.repository;

import com.econo_4factorial.newproject.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByKakaoId(Long kakaoId);

    Optional<User> findByAppleSub(String appleSub);

    boolean existsByNickname(String nickname);

    boolean existsByUserInfoPhoneNumber(String phoneNumber);

    boolean existsByUserInfoEmail(String email);
}
