package com.econo_4factorial.newproject.mountain.dto;

import com.econo_4factorial.newproject.mountain.domain.Mountain;

public record MountainDTO(
        String name,
        String location
) {
    public static MountainDTO from(Mountain mountain) {
        return new MountainDTO(
                mountain.getName(),
                mountain.getLocation()
        );
    }
}
