package com.econo_4factorial.newproject.course.controller;

import com.econo_4factorial.newproject.base.dto.CourseDetailDTO;
import com.econo_4factorial.newproject.base.dto.res.GetCourseDetailsRes;
import com.econo_4factorial.newproject.common.annotation.UserId;
import com.econo_4factorial.newproject.common.util.api.ApiResponse;
import com.econo_4factorial.newproject.common.util.api.ApiResult;
import com.econo_4factorial.newproject.course.dto.CourseWithBookmarkDTO;
import com.econo_4factorial.newproject.course.dto.res.GetCoursesRes;
import com.econo_4factorial.newproject.course.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
@Tag(name = "Course", description = "코스 관련 API")
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/mountains/{mountainId}/courses")
    @Operation(summary = "선택된 산의 코스 조회", description = "선택된 산의 모든 코스 정보를 필터링을 통해 반환합니다.")
    @Parameter(name = "mountainId", description = "산 ID", required = true)
    public ApiResult<ApiResult.SuccessBody<GetCoursesRes>> getAllCourses(
            @UserId Long userId,
            @PathVariable Long mountainId,
            @RequestParam(name = "sortBy", required = false) String sortBy
    ){
        List<CourseWithBookmarkDTO> courses = courseService.getAllCoursesWithBookmark(userId, mountainId, sortBy);
        return ApiResponse.success(GetCoursesRes.from(courses), HttpStatus.OK);
    }

    @GetMapping("/courses/{courseId}/details")
    @Operation(summary = "코스 상세 조회", description = "선택된 코스의 상세정보를 반환합니다.")
    @Parameter(name = "courseId", description = "코스 ID", required = true)
    public ApiResult<ApiResult.SuccessBody<GetCourseDetailsRes>> getCourseDetails(
            @PathVariable Long courseId
    ){
        CourseDetailDTO courseDetailDTO = courseService.getCourseDetailsByCourseId(courseId);
        return ApiResponse.success(GetCourseDetailsRes.from(courseDetailDTO),HttpStatus.OK);
    }
}
