package com.econo_4factorial.newproject.travel.controller;

import com.econo_4factorial.newproject.common.annotation.UserId;
import com.econo_4factorial.newproject.common.util.api.ApiResponse;
import com.econo_4factorial.newproject.common.util.api.ApiResult;
import com.econo_4factorial.newproject.travel.dto.TravelRecordDTO;
import com.econo_4factorial.newproject.travel.dto.res.GetTravelRecordByMonthRes;
import com.econo_4factorial.newproject.travel.service.TravelRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/travel/record")
@Tag(name = "Travel", description = "산행 기록 관련 API")
public class TravelRecordController {
    private final TravelRecordService travelRecordService;

    @GetMapping
    @Operation(summary = "산행 기록 조회", description = "월 단위로 산행 기록을 조회해옵니다")
    public ApiResult<ApiResult.SuccessBody<GetTravelRecordByMonthRes>> getPathwaysOfCourse(
            @Parameter(name = "userId", description = "사용자 ID", required = true)
            @UserId Long userId,
            @Parameter(name = "year", description = "년(year)", required = true)
            @RequestParam("year") Integer year,
            @Parameter(name = "month", description = "월(month)", required = true)
            @RequestParam("month") Integer month
    ) {
        List<TravelRecordDTO> recordList = travelRecordService.findRecordByMonth(userId, year, month);
        return ApiResponse.success(GetTravelRecordByMonthRes.from(recordList), HttpStatus.OK);
    }
}
