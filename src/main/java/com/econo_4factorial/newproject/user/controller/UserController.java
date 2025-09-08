package com.econo_4factorial.newproject.user.controller;

import com.econo_4factorial.newproject.common.annotation.UserId;
import com.econo_4factorial.newproject.common.util.api.ApiResponse;
import com.econo_4factorial.newproject.common.util.api.ApiResult;
import com.econo_4factorial.newproject.user.dto.res.GetProfileStatusRes;
import com.econo_4factorial.newproject.user.dto.res.GetRandomNicknameRes;
import com.econo_4factorial.newproject.user.service.RandomNicknameService;
import com.econo_4factorial.newproject.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/users")
@Tag(name = "User", description = "유저 관련 API")
public class UserController {
    private final UserService userService;
    private final RandomNicknameService randomNicknameService;

    @GetMapping("/profile/status")
    public ApiResult<ApiResult.SuccessBody<GetProfileStatusRes>> getProfileStatus (@UserId Long userId) {
        Boolean result = userService.isProfileFilled(userId);
        return ApiResponse.success(GetProfileStatusRes.from(result), HttpStatus.OK);
    }

    @GetMapping("/nickname/random")
    public ApiResult<ApiResult.SuccessBody<GetRandomNicknameRes>> getRandomNickname () {
        String nickname = randomNicknameService.getRandomNickname();
        return ApiResponse.success(GetRandomNicknameRes.from(nickname), HttpStatus.OK);
    }

}
