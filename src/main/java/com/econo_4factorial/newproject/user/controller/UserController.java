package com.econo_4factorial.newproject.user.controller;

import com.econo_4factorial.newproject.common.annotation.UserId;
import com.econo_4factorial.newproject.common.util.api.ApiResponse;
import com.econo_4factorial.newproject.common.util.api.ApiResult;
import com.econo_4factorial.newproject.user.dto.UserAlertSettingDTO;
import com.econo_4factorial.newproject.user.dto.req.CheckNicknameReq;
import com.econo_4factorial.newproject.user.dto.res.GetAlertSettingRes;
import com.econo_4factorial.newproject.user.dto.res.GetNicknameAvailabilityRes;
import com.econo_4factorial.newproject.user.dto.req.AddBasicInformationReq;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/profile/basic-information")
    @Operation(summary = "기본정보 등록", description = "사용자의 기본정보(닉네임, 전화번호, 이메일)를 등록합니다.")
    public ApiResult<ApiResult.SuccessBody<Void>> addBasicInformation (
            @Parameter(name = "userId", description = "사용자 ID", required = true)
            @UserId Long userId,
            @Parameter(name = "basicInformation", description = "사용자 기본정보", required = true)
            @RequestBody @Valid AddBasicInformationReq addBasicInformationReq
    ) {
        userService.registerBasicInformation(userId, addBasicInformationReq);
        return ApiResponse.success(null, HttpStatus.OK);
    }

    @GetMapping("/alert")
    @Operation(summary = "알림 설정값 조회", description = "사용자 알림 설정값을 조회합니다.")
    public ApiResult<ApiResult.SuccessBody<GetAlertSettingRes>> getAlert(
            @Parameter(name = "userId", description = "유저 아이디", required = true)
            @UserId Long userId
    ) {
        UserAlertSettingDTO userAlertSettingDTO = userService.getUserAlertSetting(userId);
        return ApiResponse.success(GetAlertSettingRes.from(userAlertSettingDTO),HttpStatus.OK);
    }

//    @PutMapping("/alert")
//    @Operation(summary = "알림 설정값 등록", description = "사용자 알림 설정값을 등록(설정)합니다.")

}
