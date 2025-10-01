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

    @Column(unique = true)
    private String nickname;

    @Column(nullable = false)
    private boolean eventAlert = true;

    @Column(nullable = false)
    private boolean travelDeviationAlert = true;

    @Column(nullable = false)
    private boolean accidentProneAreaAlert = true;

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

    public void registerBasicInformation(String nickname, String phoneNumber, String email) {
        this.nickname = nickname;
        this.userInfo.updateBasicInformation(email,phoneNumber);
    }

    public void updateAlerts(boolean event, boolean travel, boolean accident) {
        this.eventAlert = event;
        this.travelDeviationAlert = travel;
        this.accidentProneAreaAlert = accident;
    }

    public Boolean isProfileFilled() {
        return hasNickname()
                && hasEmail()
                && hasPhoneNumber();
    }

    private Boolean hasNickname() {
        return this.nickname != null;
    }

    private boolean hasEmail() {
        return this.userInfo.getEmail() != null;
    }

    private boolean hasPhoneNumber() {
        return this.userInfo.getPhoneNumber() != null;
    }

}