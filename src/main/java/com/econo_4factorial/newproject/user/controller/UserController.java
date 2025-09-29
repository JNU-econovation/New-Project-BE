package com.econo_4factorial.newproject.user.controller;

import com.econo_4factorial.newproject.common.annotation.UserId;
import com.econo_4factorial.newproject.common.util.api.ApiResponse;
import com.econo_4factorial.newproject.common.util.api.ApiResult;
import com.econo_4factorial.newproject.user.dto.req.CheckNicknameReq;
import com.econo_4factorial.newproject.user.dto.res.GetNicknameAvailabilityRes;
import com.econo_4factorial.newproject.user.dto.res.GetProfileStatusRes;
import com.econo_4factorial.newproject.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/users")
@Tag(name = "User", description = "유저 관련 API")
public class UserController {
    private final UserService userService;

    @GetMapping("/profile/status")
    @Operation(summary = "프로필 설정 여부 확인", description = "프로필 설정(기본정보/개인정보) 여부 값을 반환합니다.")
    public ApiResult<ApiResult.SuccessBody<GetProfileStatusRes>> getProfileStatus (@UserId Long userId) {
        Boolean result = userService.isProfileFilled(userId);
        return ApiResponse.success(GetProfileStatusRes.from(result), HttpStatus.OK);
    }

    @GetMapping("/nickname/check")
    @Operation(summary = "닉네임 중복 확인", description = "닉네임 중복을 체크합니다.")
    public ApiResult<ApiResult.SuccessBody<GetNicknameAvailabilityRes>> checkNicknameUnique (
            @Parameter(name = "nickname", description = "닉네임", required = true)
            @Valid CheckNicknameReq checkNicknameReq
            ) {
        boolean result = userService.isNicknameUnique(checkNicknameReq.nickname());
        return ApiResponse.success(GetNicknameAvailabilityRes.from(result), HttpStatus.OK);
    }

}
