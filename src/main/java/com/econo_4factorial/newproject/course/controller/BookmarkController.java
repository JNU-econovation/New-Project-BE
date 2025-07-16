package com.econo_4factorial.newproject.course.controller;

import com.econo_4factorial.newproject.common.annotation.UserId;
import com.econo_4factorial.newproject.common.util.api.ApiResponse;
import com.econo_4factorial.newproject.common.util.api.ApiResult;
import com.econo_4factorial.newproject.course.service.BookmarkService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookmarks")
@AllArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @PostMapping("/{courseId}")
    public ApiResult<ApiResult.SuccessBody<Void>> addBookmark (
            @UserId Long userId,
            @PathVariable(name="courseId") Long courseId
    ) {
        bookmarkService.addBookmark(userId, courseId);
        return ApiResponse.success(HttpStatus.CREATED);
    }

    @DeleteMapping({"/{courseId}"})
    public ApiResult<ApiResult.SuccessBody<Void>> deleteBookmark (
            @UserId Long userId,
            @PathVariable(name="courseId") Long courseId
    ) {
        bookmarkService.deleteBookmark(userId, courseId);
        return ApiResponse.success(HttpStatus.NO_CONTENT);
    }
}
