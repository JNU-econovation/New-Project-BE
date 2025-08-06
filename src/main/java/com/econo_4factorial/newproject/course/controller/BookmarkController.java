package com.econo_4factorial.newproject.course.controller;

import com.econo_4factorial.newproject.common.annotation.UserId;
import com.econo_4factorial.newproject.common.util.api.ApiResponse;
import com.econo_4factorial.newproject.common.util.api.ApiResult;
import com.econo_4factorial.newproject.course.dto.CourseWithBookmarkDTO;
import com.econo_4factorial.newproject.course.dto.req.AddBookmarkReq;
import com.econo_4factorial.newproject.course.dto.res.GetBookmarkListRes;
import com.econo_4factorial.newproject.course.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookmarks")
@Tag(name = "Bookmark", description = "즐겨찾기 관련 API")
@AllArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @PostMapping
    @Operation(summary = "즐겨찾기 추가", description = "사용자의 즐겨찾기를 추가합니다.")
    public ApiResult<ApiResult.SuccessBody<Void>> addBookmark (
            @Parameter(name = "userId", description = "사용자 ID", required = true)
            @UserId Long userId,

            @RequestBody @Valid AddBookmarkReq addBookmarkReq
    ) {
        bookmarkService.addBookmark(userId, addBookmarkReq.courseId());
        return ApiResponse.success(HttpStatus.CREATED);
    }

    @DeleteMapping({"/{courseId}"})
    @Operation(summary = "즐겨찾기 삭제", description = "사용자의 즐겨찾기를 삭제합니다.")
    public ApiResult<ApiResult.SuccessBody<Void>> deleteBookmark (
            @Parameter(name = "userId", description = "사용자 ID", required = true)
            @UserId Long userId,

            @Parameter(name = "courseId", description = "코스 ID", required = true)
            @PathVariable Long courseId
    ) {
        bookmarkService.deleteBookmark(userId, courseId);
        return ApiResponse.success(HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "전체 즐겨찾기 조회", description = "사용자의 모든 즐겨찾기 리스트를 조회합니다.")
    public ApiResult<ApiResult.SuccessBody<GetBookmarkListRes>> getBookmarkList (
            @Parameter(name = "userId", description = "사용자 ID", required = true)
            @UserId Long userId
    ) {
        List<CourseWithBookmarkDTO> bookmarks = bookmarkService.getBookmarkList(userId);
        return ApiResponse.success(GetBookmarkListRes.from(bookmarks),HttpStatus.OK);
    }
}
