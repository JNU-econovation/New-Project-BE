package com.econo_4factorial.newproject.user.controller;

import com.econo_4factorial.newproject.common.annotation.UserId;
import com.econo_4factorial.newproject.common.util.api.ApiResponse;
import com.econo_4factorial.newproject.common.util.api.ApiResult;
import com.econo_4factorial.newproject.user.dto.PresignedUrlDTO;
import com.econo_4factorial.newproject.user.dto.ProfileImageUrlDTO;
import com.econo_4factorial.newproject.user.dto.req.AddFileNameReq;
import com.econo_4factorial.newproject.user.dto.req.IssuePresignedUrlReq;
import com.econo_4factorial.newproject.user.dto.res.GetPresignedUrlRes;
import com.econo_4factorial.newproject.user.dto.res.GetProfileImageUrlRes;
import com.econo_4factorial.newproject.user.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/users/profile-image")
@Tag(name = "Image", description = "이미지 업로드 관련 API")
public class ImageController {

    private final S3Service s3Service;

    @Operation(summary = "S3 Presigned 주소 요청", description = "이미지 업로드할 presigned-url을 반환합니다.")
    @PostMapping
    public ApiResult<ApiResult.SuccessBody<GetPresignedUrlRes>> getPresignedUrl(
            @Parameter(hidden = true)
            @UserId Long userId,
            @RequestBody IssuePresignedUrlReq issuePresignedUrlReq
    ) {
        PresignedUrlDTO presignedUrlDTO = s3Service.createPresignedUrl(userId,issuePresignedUrlReq.imageFileFormat());
        return ApiResponse.success(GetPresignedUrlRes.from(presignedUrlDTO), HttpStatus.OK);
    }

    @Operation(summary = "프로필 이미지 조회", description = "S3에 업로드된 프로필 이미지 URL을 반환합니다.")
    @GetMapping
    public ApiResult<ApiResult.SuccessBody<GetProfileImageUrlRes>> getProfileImageUrl(
            @Parameter(hidden = true)
            @UserId Long userId
    ) {
        ProfileImageUrlDTO profileImageUrlDTO = s3Service.getFileUrl(userId);
        return ApiResponse.success(GetProfileImageUrlRes.from(profileImageUrlDTO), HttpStatus.OK);
    }

    @Operation(summary = "프로필 이미지 삭제", description = "S3에 업로드된 프로필 이미지를 삭제합니다.")
    @DeleteMapping
    public ApiResult<ApiResult.SuccessBody<Void>> deleteProfileImageUrl(
            @Parameter(hidden = true)
            @UserId Long userId
    ) {
        s3Service.deleteFileUrl(userId);
        return ApiResponse.success(null, HttpStatus.OK);
    }

    @Operation(summary = "ImageFileName DB저장", description = "파일을 Bucket에 업로드 후, 파일명을 DB에 저장합니다.")
    @PostMapping("/save")
    public ApiResult<ApiResult.SuccessBody<Void>> saveImageFileName(
            @Parameter(hidden = true)
            @UserId Long userId,
            @RequestBody AddFileNameReq addFileNameReq
    ) {
        s3Service.saveFileNameToEntity(userId, addFileNameReq.fileName());
        return ApiResponse.success(null, HttpStatus.OK);
    }
}
