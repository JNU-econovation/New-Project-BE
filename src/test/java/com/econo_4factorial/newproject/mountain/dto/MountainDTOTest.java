package com.econo_4factorial.newproject.mountain.dto;

import com.econo_4factorial.newproject.mountain.domain.Mountain;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MountainDTOTest {

    @Test
    void 산을_DTO로_변환한다() {
        Mountain mountain = mock(Mountain.class);
        when(mountain.getId()).thenReturn(1L);
        when(mountain.getName()).thenReturn("무등산");
        when(mountain.getLocation()).thenReturn("광주");
        when(mountain.getLongitude()).thenReturn(new BigDecimal("126.9890"));
        when(mountain.getLatitude()).thenReturn(new BigDecimal("35.1340"));

        MountainDTO result = MountainDTO.from(mountain);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("무등산");
        assertThat(result.location()).isEqualTo("광주");
        assertThat(result.coordinate()).containsExactly(
                new BigDecimal("126.9890"),
                new BigDecimal("35.1340")
        );
    }
}
