package com.econo_4factorial.newproject.mountain.controller;

import com.econo_4factorial.newproject.common.util.api.ApiResponse;
import com.econo_4factorial.newproject.common.util.api.ApiResult;
import com.econo_4factorial.newproject.mountain.dto.MountainDTO;
import com.econo_4factorial.newproject.mountain.dto.SuggestedMountainDTO;
import com.econo_4factorial.newproject.mountain.dto.res.GetMountainsRes;
import com.econo_4factorial.newproject.mountain.dto.res.GetSuggestedMountainRes;
import com.econo_4factorial.newproject.mountain.service.MountainService;
import com.econo_4factorial.newproject.mountain.service.SuggestMountainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/mountains")
@Tag(name = "Mountain", description = "산 관련 API")
public class MountainController {
    private final MountainService mountainService;
    private final SuggestMountainService suggestMountainService;

    @GetMapping
    @Operation(summary = "전체 산 목록 조회", description = "등록된 모든 산의 정보를 반환합니다.")
    public ApiResult<ApiResult.SuccessBody<GetMountainsRes>> getAllMountains() {
        List<MountainDTO> mountains = mountainService.findAll();
        return ApiResponse.success(GetMountainsRes.from(mountains), HttpStatus.OK);
    }

    @GetMapping("/searches/suggestions")
    @Operation(summary = "산 자동완성", description = "산 검색 시 자동완성 기능입니다.")
    public ApiResult<ApiResult.SuccessBody<GetSuggestedMountainRes>> getSuggestedMountains(
            @RequestParam("keyword") String keyword
    ){
        List<SuggestedMountainDTO> suggestedMountainDTOS = suggestMountainService.suggestMountains(keyword);
        return ApiResponse.success(GetSuggestedMountainRes.from(suggestedMountainDTOS), HttpStatus.OK);
    }
}
