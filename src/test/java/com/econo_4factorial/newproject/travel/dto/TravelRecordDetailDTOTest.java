package com.econo_4factorial.newproject.travel.dto;

import com.econo_4factorial.newproject.course.domain.Course;
import com.econo_4factorial.newproject.travel.domain.TravelRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.PrecisionModel;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

class TravelRecordDetailDTOTest {

    private GeometryFactory geometryFactory;

    @BeforeEach
    void setUp() {
        geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    }

    @Test
    void 산행상세기록을_DTO로_변환한다() {
        TravelRecord travelRecord = Mockito.mock(TravelRecord.class);
        Course course = Mockito.mock(Course.class);
        LocalDateTime startedAt = LocalDateTime.of(2024, 5, 10, 8, 30);
        LocalDateTime endAt = LocalDateTime.of(2024, 5, 10, 12, 0);
        LineString paths = geometryFactory.createLineString(new Coordinate[]{
                new Coordinate(126.0, 37.0),
                new Coordinate(126.1, 37.1)
        });

        given(travelRecord.getId()).willReturn(21L);
        given(travelRecord.getDisplayName()).willReturn("장불재 코스");
        given(travelRecord.getStartedAt()).willReturn(startedAt);
        given(travelRecord.getEndAt()).willReturn(endAt);
        given(travelRecord.getTotalTravelTime()).willReturn(Duration.ofMinutes(210));
        given(travelRecord.getTotalTravelDistanceKm()).willReturn(7.8);
        given(travelRecord.getPaths()).willReturn(paths);
        given(travelRecord.getCourse()).willReturn(course);
        given(course.getId()).willReturn(31L);

        TravelRecordDetailDTO result = TravelRecordDetailDTO.from(travelRecord);

        assertThat(result.recordId()).isEqualTo(21L);
        assertThat(result.displayName()).isEqualTo("장불재 코스");
        assertThat(result.startedAt()).isEqualTo(Timestamp.valueOf(startedAt).getTime());
        assertThat(result.endAt()).isEqualTo(Timestamp.valueOf(endAt).getTime());
        assertThat(result.duration()).isEqualTo(Duration.ofMinutes(210).toMillis());
        assertThat(result.length()).isEqualTo(7.8);
        assertThat(result.courseId()).isEqualTo(31L);
        assertThat(result.coordinates()).hasSize(2);
        assertThat(result.coordinates().get(0)).containsExactly(
                BigDecimal.valueOf(126.0),
                BigDecimal.valueOf(37.0)
        );
    }
}
