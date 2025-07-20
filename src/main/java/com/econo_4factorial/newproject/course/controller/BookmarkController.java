package com.econo_4factorial.newproject.course.controller;

import com.econo_4factorial.newproject.common.annotation.UserId;
import com.econo_4factorial.newproject.common.util.api.ApiResponse;
import com.econo_4factorial.newproject.common.util.api.ApiResult;
import com.econo_4factorial.newproject.course.dto.CourseWithBookmarkDTO;
import com.econo_4factorial.newproject.course.dto.req.AddBookmarkReq;
import com.econo_4factorial.newproject.course.dto.res.GetBookmarkListRes;
import com.econo_4factorial.newproject.course.service.BookmarkService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookmarks")
@AllArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @PostMapping
    public ApiResult<ApiResult.SuccessBody<Void>> addBookmark (
            @UserId Long userId,
            @RequestBody @Valid AddBookmarkReq addBookmarkReq
    ) {
        bookmarkService.addBookmark(userId, addBookmarkReq.courseId());
        return ApiResponse.success(HttpStatus.CREATED);
    }

    @DeleteMapping({"/{courseId}"})
    public ApiResult<ApiResult.SuccessBody<Void>> deleteBookmark (
            @UserId Long userId,
            @PathVariable(name="courseId") Long courseId
    ) {
        bookmarkService.deleteBookmark(userId, courseId);
        return ApiResponse.success(HttpStatus.OK);
    }

    @GetMapping
    public ApiResult<ApiResult.SuccessBody<GetBookmarkListRes>> getBookmarkList (
            @UserId Long userId
    ) {
        List<CourseWithBookmarkDTO> bookmarks = bookmarkService.getBookmarkList(userId);
        return ApiResponse.success(GetBookmarkListRes.from(bookmarks),HttpStatus.OK);
    }
}
