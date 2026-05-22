package com.econo_4factorial.newproject.pathway.dto.res;

import com.econo_4factorial.newproject.pathway.dto.PathwayDTO;
import java.util.List;

public record GetPathwaysOfCourseRes(
        List<PathwayCoordinatesArrayDTO> pathways
) {
    public static GetPathwaysOfCourseRes from(List<PathwayDTO> pathwayDTOList) {
        List<PathwayCoordinatesArrayDTO> pathways = pathwayDTOList.stream()
                .map(PathwayCoordinatesArrayDTO::from)
                .toList();

        return new GetPathwaysOfCourseRes(pathways);
    }
}
