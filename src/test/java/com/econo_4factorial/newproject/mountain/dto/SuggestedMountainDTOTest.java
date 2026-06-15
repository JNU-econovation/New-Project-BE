package com.econo_4factorial.newproject.mountain.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.econo_4factorial.newproject.mountain.domain.Mountain;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class SuggestedMountainDTOTest {

    @Test
    void 좌표를_경도_위도_순서로_변환한다() {
        Mountain mountain = mock(Mountain.class);
        when(mountain.getLongitude()).thenReturn(new BigDecimal("126.99"));
        when(mountain.getLatitude()).thenReturn(new BigDecimal("35.13"));

        SuggestedMountainDTO dto = SuggestedMountainDTO.from(mountain);

        assertThat(dto.coordinate()).containsExactly(
                new BigDecimal("126.99"),
                new BigDecimal("35.13")
        );
    }
}
