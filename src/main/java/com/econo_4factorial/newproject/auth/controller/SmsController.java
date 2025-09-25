package com.econo_4factorial.newproject.auth.controller;

import com.econo_4factorial.newproject.auth.dto.Req.SendSmsReq;
import com.econo_4factorial.newproject.auth.dto.Req.VerifySmsReq;
import com.econo_4factorial.newproject.auth.dto.Res.SendSmsRes;
import com.econo_4factorial.newproject.auth.dto.Res.VerifySmsRes;
import com.econo_4factorial.newproject.auth.service.SmsService;
import com.econo_4factorial.newproject.common.annotation.UserId;
import com.econo_4factorial.newproject.common.util.api.ApiResponse;
import com.econo_4factorial.newproject.common.util.api.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth/sms")
@Tag(name = "SMS", description = "SMS 인증 관련 API")
public class SmsController {
    private final SmsService smsService;

    @PostMapping
    @Operation(summary = "인증번호 전송", description = "인증번호를 전송합니다.")
    public ApiResult<ApiResult.SuccessBody<SendSmsRes>> sendSms(
            @UserId Long userId,
            @RequestBody @Valid SendSmsReq sendSmsReq
    ) {
        smsService.sendSms(sendSmsReq.phoneNumber());
        return ApiResponse.success(SendSmsRes.from(sendSmsReq.phoneNumber()),HttpStatus.OK);
    }

    @PostMapping("/verify")
    @Operation(summary = "인증번호 확인", description = "인증번호를 확인합니다.")
    public ApiResult<ApiResult.SuccessBody<VerifySmsRes>> verifySms(
            @UserId Long userId,
            @RequestBody @Valid VerifySmsReq verifySmsReq
    ) {
        smsService.verifySms(verifySmsReq.phoneNumber(),verifySmsReq.verificationCode());
        return ApiResponse.success(VerifySmsRes.from(verifySmsReq.phoneNumber()),HttpStatus.OK);
    }
}
