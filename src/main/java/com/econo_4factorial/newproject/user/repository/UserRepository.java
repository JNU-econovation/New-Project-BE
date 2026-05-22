package com.econo_4factorial.newproject.user.repository;

import com.econo_4factorial.newproject.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByKakaoId(Long kakaoId);

    Optional<User> findByAppleSub(String appleSub);

    boolean existsByNickname(String nickname);

    boolean existsByUserInfoPhoneNumber(String phoneNumber);

    boolean existsByUserInfoEmail(String email);
}
