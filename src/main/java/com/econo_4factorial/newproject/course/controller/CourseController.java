package com.econo_4factorial.newproject.course.controller;

import com.econo_4factorial.newproject.common.annotation.UserId;
import com.econo_4factorial.newproject.common.util.api.ApiResponse;
import com.econo_4factorial.newproject.common.util.api.ApiResult;
import com.econo_4factorial.newproject.course.dto.res.CourseDTO;
import com.econo_4factorial.newproject.course.dto.res.GetCoursesRes;
import com.econo_4factorial.newproject.course.service.CourseService;
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
    public ApiResult<ApiResult.SuccessBody<GetCoursesRes>> getAllCourses(
            @UserId Long userId,
            @PathVariable Long mountainId,
            @RequestParam(name = "sortBy", required = false) String sortBy
    ){
        List<CourseDTO> courses = courseService.getAllCoursesWithBookmark(userId, mountainId, sortBy);
        return ApiResponse.success(GetCoursesRes.from(courses), HttpStatus.OK);
    }
}
