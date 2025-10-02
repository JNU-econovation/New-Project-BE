package com.econo_4factorial.newproject.user.domain;

import com.econo_4factorial.newproject.common.entity.BaseEntity;
import com.econo_4factorial.newproject.user.domain.vo.PhysicalInfo;
import com.econo_4factorial.newproject.user.domain.vo.UserAlert;
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

    @Embedded
    private UserAlert userAlert = new UserAlert();

    @Embedded
    private PhysicalInfo physicalInfo;

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
        userInfo.updateBasicInformation(email,phoneNumber);
    }

    public void registerPersonalInformation(String name, Long weight, Long height, BloodType bloodType) {
        userInfo.updateName(name);
        this.physicalInfo = new PhysicalInfo(weight, height, bloodType);
    }

    public void updateUserAlert(boolean eventAlert, boolean travelDeviationAlert, boolean accidentProneAreaAlert) {
        userAlert.updateAlerts(eventAlert, travelDeviationAlert, accidentProneAreaAlert);
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