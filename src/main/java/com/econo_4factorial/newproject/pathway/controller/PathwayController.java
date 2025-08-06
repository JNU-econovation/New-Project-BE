package com.econo_4factorial.newproject.pathway.controller;

import com.econo_4factorial.newproject.common.util.api.ApiResponse;
import com.econo_4factorial.newproject.common.util.api.ApiResult;
import com.econo_4factorial.newproject.pathway.dto.PathwayDTO;
import com.econo_4factorial.newproject.pathway.dto.res.GetPathwaysOfCourseRes;
import com.econo_4factorial.newproject.pathway.service.PathwayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Pathway", description = "경로 관련 API")
public class PathwayController {
    private final PathwayService pathwayService;

    @GetMapping
    @Operation(summary = "경로 불러오기", description = "해당 코스의 경로 데이터를 불러옵니다.")
    public ApiResult<ApiResult.SuccessBody<GetPathwaysOfCourseRes>> getPathwaysOfCourse(
            @Parameter(name = "courseId", description = "코스 ID", required = true)
            @RequestParam(name = "courseId") Long courseId
    ) {
        List<PathwayDTO> pathways = pathwayService.getPathwaysByCourseId(courseId);
        return ApiResponse.success(GetPathwaysOfCourseRes.from(pathways), HttpStatus.OK);
    }
}
