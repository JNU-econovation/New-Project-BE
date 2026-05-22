package com.econo_4factorial.newproject.travel.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.econo_4factorial.newproject.course.domain.Course;
import com.econo_4factorial.newproject.travel.Status;
import com.econo_4factorial.newproject.travel.domain.TravelRecord;
import com.econo_4factorial.newproject.travel.domain.TravelTrackingInfo;
import com.econo_4factorial.newproject.travel.domain.vo.RemainingTime;
import com.econo_4factorial.newproject.user.domain.User;
import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.mockito.Mockito;

class TravelMapperTest {

    private GeometryFactory geometryFactory;

    @BeforeEach
    void setUp() {
        geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    }

    @Test
    void 기본_산행정보를_생성한다() {
        LocalDateTime startedAt = LocalDateTime.of(2024, 1, 1, 9, 0);
        Point userPoint = 포인트를_생성한다(126.0, 37.0);

        TravelTrackingInfo info = TravelMapper.toDefaultInfo(1L, 10L, startedAt, userPoint);

        assertThat(info.getUserId()).isEqualTo(1L);
        assertThat(info.getCourseId()).isEqualTo(10L);
        assertThat(info.getStartedAt()).isEqualTo(startedAt);
        assertThat(info.getStatus()).isEqualTo(Status.STARTED);
        assertThat(info.getTotalTravelDistanceKm()).isZero();
        assertThat(info.getPaths()).hasSize(1);
        assertThat(info.getLastPoint()).isEqualTo(userPoint);
    }

    @Test
    void 산행정보를_산행기록으로_변환한다() {
        User user = Mockito.mock(User.class);
        Course course = Mockito.mock(Course.class);
        TravelTrackingInfo info = TravelMapper.toDefaultInfo(
                1L,
                10L,
                LocalDateTime.of(2024, 1, 1, 9, 0),
                포인트를_생성한다(126.0, 37.0)
        );
        info.currentPosition(포인트를_생성한다(126.1, 37.1), 3.2,
                new RemainingTime(Duration.ofMinutes(20), Duration.ofMinutes(40)));
        info.end(LocalDateTime.of(2024, 1, 1, 12, 0), 포인트를_생성한다(126.2, 37.2), 6.4,
                new RemainingTime(Duration.ZERO, Duration.ZERO), Duration.ofHours(3));

        TravelRecord travelRecord = TravelMapper.toRecord(user, course, info, "테스트 코스");

        assertThat(travelRecord.getUser()).isEqualTo(user);
        assertThat(travelRecord.getCourse()).isEqualTo(course);
        assertThat(travelRecord.getDisplayName()).isEqualTo("테스트 코스");
        assertThat(travelRecord.getStartedAt()).isEqualTo(info.getStartedAt());
        assertThat(travelRecord.getEndAt()).isEqualTo(info.getEndAt());
        assertThat(travelRecord.getTotalTravelDistanceKm()).isEqualTo(6.4);
        assertThat(travelRecord.getTotalTravelTime()).isEqualTo(Duration.ofHours(3));
        assertThat(travelRecord.getPaths().getNumPoints()).isEqualTo(3);
    }

    private Point 포인트를_생성한다(double x, double y) {
        return geometryFactory.createPoint(new Coordinate(x, y));
    }
}
