package com.econo_4factorial.newproject.travel.dto.res;

import static org.assertj.core.api.Assertions.assertThat;

import com.econo_4factorial.newproject.travel.dto.TravelRecordDetailDTO;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class GetTravelRecordDetailResTest {

    @Test
    void 산행상세응답을_생성한다() {
        TravelRecordDetailDTO recordDetail = new TravelRecordDetailDTO(
                3L,
                "원효사 코스",
                1000L,
                2000L,
                1000L,
                6.7,
                List.of(List.of(BigDecimal.valueOf(126.0), BigDecimal.valueOf(37.0))),
                8L
        );

        GetTravelRecordDetailRes result = GetTravelRecordDetailRes.from(recordDetail);

        assertThat(result.recordId()).isEqualTo(3L);
        assertThat(result.displayName()).isEqualTo("원효사 코스");
        assertThat(result.startedAt()).isEqualTo(1000L);
        assertThat(result.endAt()).isEqualTo(2000L);
        assertThat(result.duration()).isEqualTo(1000L);
        assertThat(result.length()).isEqualTo(6.7);
        assertThat(result.coordinates()).containsExactlyElementsOf(recordDetail.coordinates());
        assertThat(result.courseId()).isEqualTo(8L);
        assertThat(result.mountainId()).isEqualTo(GetTravelRecordDetailRes.DEFAULT_MOUNTAIN_ID);
    }
}
