package com.econo_4factorial.newproject.travel.dto;

import com.econo_4factorial.newproject.common.constant.Difficulty;
import com.econo_4factorial.newproject.course.domain.Course;
import com.econo_4factorial.newproject.travel.domain.TravelRecord;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

class TravelRecordDTOTest {

    @Test
    void 산행기록을_DTO로_변환한다() {
        TravelRecord travelRecord = Mockito.mock(TravelRecord.class);
        Course course = Mockito.mock(Course.class);
        LocalDateTime startedAt = LocalDateTime.of(2024, 5, 10, 8, 30);

        given(travelRecord.getId()).willReturn(11L);
        given(travelRecord.getStartedAt()).willReturn(startedAt);
        given(travelRecord.getCourse()).willReturn(course);
        given(course.getDisplayName()).willReturn("무등산 원효사 코스");
        given(course.getImageUrl()).willReturn("/course.png");
        given(course.getLength()).willReturn(6.4);
        given(course.getDuration()).willReturn(240L);
        given(course.getDifficulty()).willReturn(Difficulty.NORMAL);

        TravelRecordDTO result = TravelRecordDTO.from(travelRecord);

        assertThat(result.id()).isEqualTo(11L);
        assertThat(result.date()).isEqualTo(Timestamp.valueOf(startedAt).getTime());
        assertThat(result.displayName()).isEqualTo("무등산 원효사 코스");
        assertThat(result.image()).isEqualTo("/course.png");
        assertThat(result.length()).isEqualTo(6.4);
        assertThat(result.duration()).isEqualTo(240L);
        assertThat(result.difficulty()).isEqualTo(Difficulty.NORMAL);
    }
}
