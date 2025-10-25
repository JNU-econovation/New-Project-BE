package com.econo_4factorial.newproject.travel.controller;

import com.econo_4factorial.newproject.common.annotation.UserId;
import com.econo_4factorial.newproject.common.util.api.ApiResponse;
import com.econo_4factorial.newproject.common.util.api.ApiResult;
import com.econo_4factorial.newproject.travel.dto.TravelRecordDTO;
import com.econo_4factorial.newproject.travel.dto.TravelRecordDetailDTO;
import com.econo_4factorial.newproject.travel.dto.res.GetTravelRecordByMonthRes;
import com.econo_4factorial.newproject.travel.dto.res.GetTravelRecordDetailRes;
import com.econo_4factorial.newproject.travel.service.TravelRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{recordId}/details")
    @Operation(summary = "산행 기록 상세조회", description = "특정 산행 기록을 상세조회 합니다")
    public ApiResult<ApiResult.SuccessBody<GetTravelRecordDetailRes>> getPathwaysOfCourse(
            @Parameter(name = "userId", description = "사용자 ID", required = true)
            @UserId Long userId,

            @Parameter(name = "recordId", description = "산행 기록 ID", required = true)
            @PathVariable Long recordId
    ) {
        TravelRecordDetailDTO recordDetail = travelRecordService.findRecordById(recordId);
        return ApiResponse.success(GetTravelRecordDetailRes.from(recordDetail), HttpStatus.OK);
    }
}
