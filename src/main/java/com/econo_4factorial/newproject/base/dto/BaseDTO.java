package com.econo_4factorial.newproject.base.dto;


import com.econo_4factorial.newproject.base.domain.Base;

import java.math.BigDecimal;
import java.util.List;

public record BaseDTO(
        Long baseId,
        String name,
        List<BigDecimal> coordinate

) {
    public static BaseDTO from(Base base) {
        return new BaseDTO(
                base.getId(),
                base.getName(),
                List.of(base.getLatitude(),base.getLongitude())
        );
    }
}
