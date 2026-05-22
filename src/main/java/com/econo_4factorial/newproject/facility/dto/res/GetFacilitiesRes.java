package com.econo_4factorial.newproject.facility.dto.res;

import com.econo_4factorial.newproject.facility.dto.FacilityDTO;
import java.util.List;

public record GetFacilitiesRes(
        Long mountainId,
        List<FacilityDTO> facilities
) {
    public static GetFacilitiesRes from(Long mountainId, List<FacilityDTO> facilities) {
        return new GetFacilitiesRes(mountainId, facilities);
    }
}
