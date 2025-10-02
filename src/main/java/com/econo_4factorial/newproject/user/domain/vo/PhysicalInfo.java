package com.econo_4factorial.newproject.user.domain.vo;

import com.econo_4factorial.newproject.common.exception.ValidationMessage;
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

    public void updatePersonalInformation(Long weight, Long height, BloodType bloodType) {
        this.weight = weight;
        this.height = height;
        this.bloodType = bloodType;
    }
}
