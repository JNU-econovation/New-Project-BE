package com.econo_4factorial.newproject.facility.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.econo_4factorial.newproject.facility.domain.Facility;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class FacilityDTOTest {

    @Test
    void 좌표를_경도_위도_순서로_변환한다() {
        Facility facility = mock(Facility.class);
        when(facility.getLongitude()).thenReturn(new BigDecimal("126.99"));
        when(facility.getLatitude()).thenReturn(new BigDecimal("35.13"));

        FacilityDTO dto = FacilityDTO.from(facility);

        assertThat(dto.coordinate()).containsExactly(
                new BigDecimal("126.99"),
                new BigDecimal("35.13")
        );
    }
}
