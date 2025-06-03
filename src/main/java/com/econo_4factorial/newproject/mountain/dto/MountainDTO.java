package com.econo_4factorial.newproject.mountain.dto;

import com.econo_4factorial.newproject.mountain.domain.Mountain;

public record MountainDTO(
        Long id,
        String name,
        String location
) {
    public static MountainDTO from(Mountain mountain) {
        return new MountainDTO(
                mountain.getId(),
                mountain.getName(),
                mountain.getLocation()
        );
    }
}
