package com.econo_4factorial.newproject.pathway.controller;

import com.econo_4factorial.newproject.common.util.api.ApiResponse;
import com.econo_4factorial.newproject.common.util.api.ApiResult;
import com.econo_4factorial.newproject.pathway.dto.PathwayDTO;
import com.econo_4factorial.newproject.pathway.dto.res.GetPathwaysOfCourseRes;
import com.econo_4factorial.newproject.pathway.service.PathwayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/pathways")
public class PathwayController {
    private final PathwayService pathwayService;

    @GetMapping
    public ApiResult<ApiResult.SuccessBody<GetPathwaysOfCourseRes>> getPathwaysOfCourse(
            @RequestParam(name = "courseId") Long courseId
    ) {
        List<PathwayDTO> pathways = pathwayService.getPathwaysByCourseId(courseId);
        return ApiResponse.success(GetPathwaysOfCourseRes.from(pathways), HttpStatus.OK);
    }
}
