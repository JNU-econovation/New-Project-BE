package com.econo_4factorial.newproject.base.controller;

import com.econo_4factorial.newproject.base.dto.BaseDTO;
import com.econo_4factorial.newproject.base.dto.BaseDetailDTO;
import com.econo_4factorial.newproject.base.dto.res.GetBasesDetailsRes;
import com.econo_4factorial.newproject.base.dto.res.GetBasesRes;
import com.econo_4factorial.newproject.base.service.BaseService;
import com.econo_4factorial.newproject.common.util.api.ApiResponse;
import com.econo_4factorial.newproject.common.util.api.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/bases")
@Tag(name = "Base", description = "거점 관련 API")
public class BaseController {
    private final BaseService baseService;

    @GetMapping
    @Operation(summary = "전체 거점 목록 조회", description = "해당 산의 모든 거점 경도, 위도를 반환합니다.")
    public ApiResult<ApiResult.SuccessBody<GetBasesRes>> getBasesByMountain(
            @Parameter(name = "mountainId", description = "산 ID", required = true)
            @RequestParam Long mountainId
    ) {
        List<BaseDTO> baseDTOS = baseService.getBasesByMountainId(mountainId);
        return ApiResponse.success(GetBasesRes.from(mountainId, baseDTOS), HttpStatus.OK);
    }

    @GetMapping("/{mountainId}/details")
    @Operation(summary = "전체 거점 상세 조회", description = "선택된 산의 모든 거점 상세정보를 반환합니다.")
    public ApiResult<ApiResult.SuccessBody<GetBasesDetailsRes>> getBaseDetailsByMountain(
            @Parameter(name = "mountainId", description = "산 ID", required = true)
            @PathVariable Long mountainId
    ) {
        List<BaseDetailDTO> baseDetailDTOS = baseService.getBaseDetailsByMountainId(mountainId);
        return ApiResponse.success(GetBasesDetailsRes.from(mountainId, baseDetailDTOS), HttpStatus.OK);
    }
}
