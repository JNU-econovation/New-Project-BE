package com.econo_4factorial.newproject.travel.dto.res;

import com.econo_4factorial.newproject.common.constant.Difficulty;
import com.econo_4factorial.newproject.travel.dto.TravelRecordDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GetTravelRecordByMonthResTest {

    @Test
    void 월별_산행기록_응답을_생성한다() {
        List<TravelRecordDTO> records = List.of(
                new TravelRecordDTO(1L, 1000L, "코스1", "/1.png", 2.3, 120L, Difficulty.EASY),
                new TravelRecordDTO(2L, 2000L, "코스2", "/2.png", 4.5, 240L, Difficulty.NORMAL)
        );

        GetTravelRecordByMonthRes result = GetTravelRecordByMonthRes.from(records);

        assertThat(result.records()).containsExactlyElementsOf(records);
    }
}
