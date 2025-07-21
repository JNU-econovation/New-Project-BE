package com.econo_4factorial.newproject.pathway.dto.res;

import com.econo_4factorial.newproject.pathway.dto.PathwayDTO;

import java.util.List;

public record GetPathwaysOfCourseRes(
        List<PathwayDTO> pathways
) {
    public static GetPathwaysOfCourseRes from(List<PathwayDTO> pathways) {
        return new GetPathwaysOfCourseRes(pathways);
    }
}
