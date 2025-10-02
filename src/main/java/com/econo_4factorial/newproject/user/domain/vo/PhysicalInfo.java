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
    @Column(name = "blood_type")
    private BloodType bloodType;

    public void updatePersonalInformation(Long weight, Long height, String bloodType) {
        this.weight = weight;
        this.height = height;
        this.bloodType = BloodType.valueOf(bloodType);
    }
    // heigh, weight, 혈액형 검증하는 것도 있어야 하지 않나..?
}
