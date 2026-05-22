package com.econo_4factorial.newproject.pathway.dto;

import com.econo_4factorial.newproject.common.constant.Difficulty;
import lombok.Builder;
import org.locationtech.jts.geom.LineString;

@Builder
public record PathwayDTO(
        Long pathwayId,
        Long deptBaseId,
        Long destBaseId,
        Difficulty difficulty,
        LineString coordinates
) {
}
