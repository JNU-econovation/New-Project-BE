package com.econo_4factorial.newproject.mountain.controller;

import com.econo_4factorial.newproject.common.util.api.ApiResponse;
import com.econo_4factorial.newproject.common.util.api.ApiResult;
import com.econo_4factorial.newproject.mountain.dto.MountainDTO;
import com.econo_4factorial.newproject.mountain.dto.res.GetMountainsRes;
import com.econo_4factorial.newproject.mountain.service.MountainService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/mountains")
public class MountainController {
    private final MountainService mountainService;

    @GetMapping
    public ApiResult<ApiResult.SuccessBody<GetMountainsRes>> getAllMountains() {
        List<MountainDTO> mountains = mountainService.findAll();
        return ApiResponse.success(GetMountainsRes.from(mountains), HttpStatus.OK);
    }
}
