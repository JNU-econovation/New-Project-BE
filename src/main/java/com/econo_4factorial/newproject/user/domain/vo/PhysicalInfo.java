package com.econo_4factorial.newproject.user.domain.vo;

import com.econo_4factorial.newproject.user.domain.BloodType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class PhysicalInfo {

    @Column(name = "weight")
    private Long weight;

    @Column(name = "height")
    private Long height;

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_type", length = 2)
    private BloodType bloodType;

    @Column(name = "etc", length = 200)
    private String etc;

    public PhysicalInfo(Long weight, Long height, BloodType bloodType, String etc) {
        validate(weight, height, etc);
        this.weight = weight;
        this.height = height;
        this.bloodType = bloodType;
        this.etc = etc;
    }

    public void updatePersonalInformation(Long weight, Long height, BloodType bloodType, String etc) {
        validate(weight, height, etc);
        this.weight = weight;
        this.height = height;
        this.bloodType = bloodType;
        this.etc = etc;
    }

    private static void validate(Long weight, Long height, String etc) {
        validateRange(weight, 1L, 500L, "몸무게는 1~500kg 범위여야 합니다.");
        validateRange(height, 30L, 300L, "키는 30~300cm 범위여야 합니다.");
        validateEtc(etc);
    }

    private static void validateRange(Long value, Long min, Long max, String msg) {
        if (value < min || value > max) throw new IllegalArgumentException(msg);
    }

    private static void validateEtc(String etc) {
        if (etc != null && etc.length() > 200) {
            throw new IllegalArgumentException("비고(etc)는 200자 이하여야 합니다.");
        }
    }

    public boolean isPersonalInfoSet() {
        return weight != null && height != null && bloodType != null;
    }
}
