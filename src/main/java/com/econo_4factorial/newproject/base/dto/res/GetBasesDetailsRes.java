package com.econo_4factorial.newproject.base.dto.res;

import com.econo_4factorial.newproject.base.dto.BaseDetailDTO;

import java.util.List;

public record GetBasesDetailsRes(
        Long mountainId,
        List<BaseDetailDTO> baseDetails
) {
    public static GetBasesDetailsRes from(Long mountainId, List<BaseDetailDTO> baseDetails) {
        return new GetBasesDetailsRes(mountainId,baseDetails);
    }
}
