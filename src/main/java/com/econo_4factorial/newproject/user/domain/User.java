package com.econo_4factorial.newproject.user.domain;

import com.econo_4factorial.newproject.common.entity.BaseEntity;
import com.econo_4factorial.newproject.user.domain.vo.UserInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "email", column = @Column(name = "email", unique = true)),
            @AttributeOverride(name = "name", column = @Column(name = "name")),
            @AttributeOverride(name = "phoneNumber", column = @Column(name = "phone_number"))
    })
    private UserInfo userInfo;

    @Column(unique = true)
    private Long kakaoId;

    @Column(unique = true)
    private String appleSub;

    private String nickname;

    @Builder(builderMethodName = "kakaoUserBuilder", builderClassName = "kakaoUserBuilder")
    public User(String email, String name, Long kakaoId) {
        this.userInfo = new UserInfo(email, name);
        this.kakaoId = kakaoId;
    }

    @Builder(builderMethodName = "appleUserBuilder", builderClassName = "appleUserBuilder")
    public User(String email, String name, String appleSub) {
        this.userInfo = new UserInfo(email, name);
        this.appleSub = appleSub;
    }

    public Boolean isProfileComplete() {
        return isNicknameNotNull()
        && isEmailNotNull()
        && isPhoneNumberNotNull();
    }

    private Boolean isNicknameNotNull() {
        return this.nickname != null;
    }

    private boolean isEmailNotNull() {
        return this.userInfo.getEmail() != null;
    }

    private boolean isPhoneNumberNotNull() {
        return this.userInfo.getPhoneNumber() != null;
    }

}