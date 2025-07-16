package com.econo_4factorial.newproject.facility.dto;

import com.econo_4factorial.newproject.facility.domain.Facility;
import com.econo_4factorial.newproject.facility.domain.FacilityType;

import java.math.BigDecimal;
import java.util.List;

public record FacilityDTO(
        Long facilityId,
        String facilityName,
        FacilityType facilityType,
        List<BigDecimal> coordinate

) {
    public static FacilityDTO from(Facility facility) {
        return new FacilityDTO(
                facility.getId(),
                facility.getName(),
                facility.getType(),
                List.of(facility.getLatitude(), facility.getLongitude())
        );
    }
}
