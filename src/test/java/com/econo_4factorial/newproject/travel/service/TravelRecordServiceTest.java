package com.econo_4factorial.newproject.travel.service;

import com.econo_4factorial.newproject.common.constant.Difficulty;
import com.econo_4factorial.newproject.course.domain.Course;
import com.econo_4factorial.newproject.course.service.CourseService;
import com.econo_4factorial.newproject.travel.domain.TravelRecord;
import com.econo_4factorial.newproject.travel.domain.TravelTrackingInfo;
import com.econo_4factorial.newproject.travel.dto.TravelRecordDTO;
import com.econo_4factorial.newproject.travel.dto.TravelRecordDetailDTO;
import com.econo_4factorial.newproject.travel.exception.TravelRecordNotFoundException;
import com.econo_4factorial.newproject.travel.repository.TravelRecordRepository;
import com.econo_4factorial.newproject.user.domain.User;
import com.econo_4factorial.newproject.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TravelRecordServiceTest {

    @Mock
    private TravelRecordRepository travelRecordRepository;

    @Mock
    private UserService userService;

    @Mock
    private CourseService courseService;

    private TravelRecordService travelRecordService;
    private GeometryFactory geometryFactory;

    @BeforeEach
    void setUp() {
        travelRecordService = new TravelRecordService(travelRecordRepository, userService, courseService);
        geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    }

    @Test
    void 월별_산행기록을_조회해_DTO로_반환한다() {
        Course course = org.mockito.Mockito.mock(Course.class);
        given(course.getDisplayName()).willReturn("대표 코스");
        given(course.getImageUrl()).willReturn("/image.png");
        given(course.getLength()).willReturn(4.5);
        given(course.getDuration()).willReturn(120L);
        given(course.getDifficulty()).willReturn(Difficulty.NORMAL);
        TravelRecord travelRecord = TravelRecord.builder()
                .user(org.mockito.Mockito.mock(User.class))
                .course(course)
                .paths(geometryFactory.createLineString(new Coordinate[]{
                        new Coordinate(126.0, 37.0),
                        new Coordinate(126.1, 37.1)
                }))
                .startedAt(LocalDateTime.of(2024, 5, 10, 9, 0))
                .endAt(LocalDateTime.of(2024, 5, 10, 12, 0))
                .totalTravelDistanceKm(4.5)
                .totalTravelTime(Duration.ofHours(3))
                .displayName("기록명")
                .build();
        ReflectionTestUtils.setField(travelRecord, "id", 55L);

        given(travelRecordRepository.findAllByUserIdAndStartedAtBetween(
                org.mockito.ArgumentMatchers.eq(1L),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).willReturn(List.of(travelRecord));

        List<TravelRecordDTO> result = travelRecordService.findRecordByMonth(1L, 2024, 5);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().id()).isEqualTo(55L);
        assertThat(result.getFirst().displayName()).isEqualTo("대표 코스");
        assertThat(result.getFirst().image()).isEqualTo("/image.png");
        assertThat(result.getFirst().length()).isEqualTo(4.5);
    }

    @Test
    void 산행정보를_기록으로_저장한다() {
        User user = org.mockito.Mockito.mock(User.class);
        Course course = org.mockito.Mockito.mock(Course.class);
        given(course.getDisplayName()).willReturn("정상 코스");
        TravelTrackingInfo info = TravelTrackingInfo.builder()
                .userId(1L)
                .courseId(201L)
                .startedAt(LocalDateTime.of(2024, 1, 1, 9, 0))
                .paths(new ArrayList<>(List.of(
                        포인트를_생성한다(126.0, 37.0),
                        포인트를_생성한다(126.1, 37.1)
                )))
                .build();
        info.end(LocalDateTime.of(2024, 1, 1, 12, 0), 포인트를_생성한다(126.2, 37.2), 6.4,
                new com.econo_4factorial.newproject.travel.domain.vo.RemainingTime(Duration.ZERO, Duration.ZERO),
                Duration.ofHours(3));

        given(userService.findUserByIdOrThrow(1L)).willReturn(user);
        given(courseService.findByIdOrThrow(201L)).willReturn(course);

        travelRecordService.saveTravelRecord(info);

        ArgumentCaptor<TravelRecord> captor = ArgumentCaptor.forClass(TravelRecord.class);
        verify(travelRecordRepository).save(captor.capture());
        TravelRecord savedRecord = captor.getValue();

        assertThat(savedRecord.getUser()).isEqualTo(user);
        assertThat(savedRecord.getCourse()).isEqualTo(course);
        assertThat(savedRecord.getDisplayName()).isEqualTo("정상 코스");
        assertThat(savedRecord.getStartedAt()).isEqualTo(info.getStartedAt());
        assertThat(savedRecord.getEndAt()).isEqualTo(info.getEndAt());
        assertThat(savedRecord.getTotalTravelDistanceKm()).isEqualTo(6.4);
        assertThat(savedRecord.getTotalTravelTime()).isEqualTo(Duration.ofHours(3));
    }

    @Test
    void 기록_ID로_상세기록을_조회한다() {
        Course course = org.mockito.Mockito.mock(Course.class);
        given(course.getId()).willReturn(301L);
        TravelRecord travelRecord = TravelRecord.builder()
                .user(org.mockito.Mockito.mock(User.class))
                .course(course)
                .paths(geometryFactory.createLineString(new Coordinate[]{
                        new Coordinate(126.0, 37.0),
                        new Coordinate(126.1, 37.1)
                }))
                .startedAt(LocalDateTime.of(2024, 2, 1, 8, 0))
                .endAt(LocalDateTime.of(2024, 2, 1, 10, 0))
                .totalTravelDistanceKm(5.2)
                .totalTravelTime(Duration.ofHours(2))
                .displayName("상세 코스")
                .build();
        ReflectionTestUtils.setField(travelRecord, "id", 77L);

        given(travelRecordRepository.findByIdAndUserId(77L, 1L)).willReturn(Optional.of(travelRecord));

        TravelRecordDetailDTO result = travelRecordService.findRecordById(1L, 77L);

        assertThat(result.recordId()).isEqualTo(77L);
        assertThat(result.displayName()).isEqualTo("상세 코스");
        assertThat(result.startedAt()).isEqualTo(Timestamp.valueOf(LocalDateTime.of(2024, 2, 1, 8, 0)).getTime());
        assertThat(result.endAt()).isEqualTo(Timestamp.valueOf(LocalDateTime.of(2024, 2, 1, 10, 0)).getTime());
        assertThat(result.duration()).isEqualTo(Duration.ofHours(2).toMillis());
        assertThat(result.courseId()).isEqualTo(301L);
        assertThat(result.coordinates()).hasSize(2);
        assertThat(result.length()).isEqualTo(5.2);
    }

    @Test
    void 없는_기록을_조회하면_예외가_발생한다() {
        given(travelRecordRepository.findByIdAndUserId(99L, 1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> travelRecordService.findRecordById(1L, 99L))
                .isInstanceOf(TravelRecordNotFoundException.class);
    }

    @Test
    void 사용자_소유_기록을_삭제한다() {
        TravelRecord travelRecord = TravelRecord.builder()
                .user(org.mockito.Mockito.mock(User.class))
                .course(org.mockito.Mockito.mock(Course.class))
                .displayName("삭제 대상")
                .build();
        ReflectionTestUtils.setField(travelRecord, "id", 88L);

        given(travelRecordRepository.findByIdAndUserId(88L, 1L)).willReturn(Optional.of(travelRecord));

        travelRecordService.deleteRecordById(1L, 88L);

        verify(travelRecordRepository).delete(travelRecord);
    }

    @Test
    void 사용자_소유가_아닌_기록을_삭제하면_예외가_발생한다() {
        given(travelRecordRepository.findByIdAndUserId(88L, 1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> travelRecordService.deleteRecordById(1L, 88L))
                .isInstanceOf(TravelRecordNotFoundException.class);
    }

    private Point 포인트를_생성한다(double x, double y) {
        return geometryFactory.createPoint(new Coordinate(x, y));
    }
}
