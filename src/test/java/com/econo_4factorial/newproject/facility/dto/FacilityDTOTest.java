package com.econo_4factorial.newproject.facility.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.econo_4factorial.newproject.facility.domain.Facility;
import com.econo_4factorial.newproject.facility.domain.FacilityType;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class FacilityDTOTest {

    @Test
    void 시설을_DTO로_변환한다() {
        Facility facility = mock(Facility.class);
        when(facility.getId()).thenReturn(3L);
        when(facility.getName()).thenReturn("중머리재 화장실");
        when(facility.getType()).thenReturn(FacilityType.TOILET);
        when(facility.getLongitude()).thenReturn(new BigDecimal("126.9931"));
        when(facility.getLatitude()).thenReturn(new BigDecimal("35.1402"));

        FacilityDTO result = FacilityDTO.from(facility);

        assertThat(result.facilityId()).isEqualTo(3L);
        assertThat(result.facilityName()).isEqualTo("중머리재 화장실");
        assertThat(result.facilityType()).isEqualTo(FacilityType.TOILET);
        assertThat(result.coordinate()).containsExactly(
                new BigDecimal("126.9931"),
                new BigDecimal("35.1402")
        );
    }
}
