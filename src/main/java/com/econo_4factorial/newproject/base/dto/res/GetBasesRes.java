package com.econo_4factorial.newproject.base.dto.res;

import com.econo_4factorial.newproject.base.dto.BaseDTO;
import java.util.List;

public record GetBasesRes(
        Long mountainId,
        List<BaseDTO> bases
) {
    public static GetBasesRes from(Long mountainId, List<BaseDTO> bases) {
        return new GetBasesRes(mountainId, bases);
    }
}
