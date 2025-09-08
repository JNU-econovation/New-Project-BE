package com.econo_4factorial.newproject.mountain.dto;

import com.econo_4factorial.newproject.mountain.domain.Mountain;

import java.math.BigDecimal;
import java.util.List;

public record SuggestedMountainDTO(
        Long id,
        String name,
        List<BigDecimal> coordinate
) {
    public static SuggestedMountainDTO from(Mountain mountain) {
        return new SuggestedMountainDTO(
                mountain.getId(),
                mountain.getName(),
                List.of(mountain.getLongitude(), mountain.getLatitude())
        );
    }
}