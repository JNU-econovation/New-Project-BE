package com.econo_4factorial.newproject.user.domain;

import com.econo_4factorial.newproject.common.exception.NullRequestException;
import com.econo_4factorial.newproject.user.exeception.BadRequestException.InvalidBloodTypeException;

public enum BloodType {
    A,
    B,
    O,
    AB;

    public static BloodType fromString(String bloodType) {
        if (bloodType == null) throw new NullRequestException();
        try {
            return BloodType.valueOf(bloodType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidBloodTypeException();
        }
    }
}
