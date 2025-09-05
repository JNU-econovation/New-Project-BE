package com.econo_4factorial.newproject.mountain.dto;

import com.econo_4factorial.newproject.mountain.domain.Mountain;

import java.math.BigDecimal;
import java.util.List;

public record MountainDTO(
        Long id,
        String name,
        String location,
        List<BigDecimal> coordinate
) {
    public static MountainDTO from(Mountain mountain) {
        return new MountainDTO(
                mountain.getId(),
                mountain.getName(),
                mountain.getLocation(),
                List.of(mountain.getLongitude(), mountain.getLatitude())
        );
    }
}
