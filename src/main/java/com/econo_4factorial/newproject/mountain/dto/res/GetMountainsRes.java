package com.econo_4factorial.newproject.mountain.dto.res;

import com.econo_4factorial.newproject.mountain.dto.MountainDTO;

import java.util.List;

public record GetMountainsRes(
        List<MountainDTO> mountains
) {
    public static GetMountainsRes from(List<MountainDTO> mountains) {
        return new GetMountainsRes(mountains);
    }
}
