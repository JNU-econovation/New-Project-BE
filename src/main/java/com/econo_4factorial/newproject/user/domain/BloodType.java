package com.econo_4factorial.newproject.user.domain;

import com.econo_4factorial.newproject.user.exception.BadRequestException.InvalidBloodTypeException;

public enum BloodType {
    A,
    B,
    O,
    AB;

    public static BloodType fromString(String bloodType) {
        if (bloodType == null) throw new IllegalArgumentException("bloodType cannot be null");
        try {
            return BloodType.valueOf(bloodType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidBloodTypeException();
        }
    }
}
