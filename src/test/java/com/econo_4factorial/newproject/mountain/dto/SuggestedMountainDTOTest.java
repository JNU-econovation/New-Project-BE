package com.econo_4factorial.newproject.mountain.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.econo_4factorial.newproject.mountain.domain.Mountain;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class SuggestedMountainDTOTest {

    @Test
    void 산을_자동완성_DTO로_변환한다() {
        Mountain mountain = mock(Mountain.class);
        when(mountain.getId()).thenReturn(2L);
        when(mountain.getName()).thenReturn("북한산");
        when(mountain.getLongitude()).thenReturn(new BigDecimal("126.9805"));
        when(mountain.getLatitude()).thenReturn(new BigDecimal("37.6587"));

        SuggestedMountainDTO result = SuggestedMountainDTO.from(mountain);

        assertThat(result.id()).isEqualTo(2L);
        assertThat(result.name()).isEqualTo("북한산");
        assertThat(result.coordinate()).containsExactly(
                new BigDecimal("126.9805"),
                new BigDecimal("37.6587")
        );
    }
}
