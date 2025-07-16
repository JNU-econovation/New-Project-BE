package com.econo_4factorial.newproject.facility.controller;

import com.econo_4factorial.newproject.common.util.api.ApiResponse;
import com.econo_4factorial.newproject.common.util.api.ApiResult;
import com.econo_4factorial.newproject.facility.dto.FacilityDTO;
import com.econo_4factorial.newproject.facility.dto.res.GetFacilitiesRes;
import com.econo_4factorial.newproject.facility.service.FacilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/facilities")
@Tag(name = "Facility", description = "시설 관련 API")
public class FacilityController {
    private final FacilityService facilityService;

    @GetMapping
    @Operation(summary = "전체 시설 목록 조회", description = "해당 산의 모든 시설들의 위도, 경도를 반환합니다.")
    @Parameter(name = "mountainId", description = "산 ID", required = true)
    public ApiResult<ApiResult.SuccessBody<GetFacilitiesRes>> getFacilitiesByMountain(@RequestParam Long mountainId) {
        List<FacilityDTO> facilityDTOS = facilityService.getFacilitiesByMountainId(mountainId);
        return ApiResponse.success(GetFacilitiesRes.from(mountainId, facilityDTOS), HttpStatus.OK);
    }
}
