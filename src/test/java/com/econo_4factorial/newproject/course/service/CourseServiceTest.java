package com.econo_4factorial.newproject.course.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.econo_4factorial.newproject.common.constant.Difficulty;
import com.econo_4factorial.newproject.course.domain.Course;
import com.econo_4factorial.newproject.course.dto.ClosestCoordinateInfo;
import com.econo_4factorial.newproject.course.dto.CourseSearchCondition;
import com.econo_4factorial.newproject.course.dto.CourseWithBookmarkDTO;
import com.econo_4factorial.newproject.course.exception.BadRequestException.CourseNotFoundException;
import com.econo_4factorial.newproject.course.repository.CourseRepository;
import com.econo_4factorial.newproject.pathway.dto.PathwayDTO;
import com.econo_4factorial.newproject.pathway.service.PathwayService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private PathwayService pathwayService;

    @Mock
    private CourseLocationMatcher courseLocationMatcher;

    private CourseService courseService;
    private GeometryFactory geometryFactory;

    @BeforeEach
    void setUp() {
        courseService = new CourseService(courseRepository, pathwayService, courseLocationMatcher);
        geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    }

    @Test
    void 코스를_ID로_조회한다() {
        Course course = org.mockito.Mockito.mock(Course.class);
        given(courseRepository.findById(1L)).willReturn(Optional.of(course));

        Course result = courseService.findByIdOrThrow(1L);

        assertThat(result).isEqualTo(course);
    }

    @Test
    void 없는_코스를_조회하면_예외가_발생한다() {
        given(courseRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.findByIdOrThrow(99L))
                .isInstanceOf(CourseNotFoundException.class);
    }

    @Test
    void 산과_정렬조건으로_북마크포함_코스목록을_조회한다() {
        List<CourseWithBookmarkDTO> courses = List.of(
                new CourseWithBookmarkDTO(1L, "c1", "코스1", 10L, 5.3, 180L, Difficulty.EASY, true, "/1.png", 100L)
        );
        ArgumentCaptor<CourseSearchCondition> conditionCaptor = ArgumentCaptor.forClass(CourseSearchCondition.class);
        given(courseRepository.findAllByMountainIdWithBookmark(conditionCaptor.capture(), eq(100L), eq(7L)))
                .willReturn(courses);

        List<CourseWithBookmarkDTO> result = courseService.getAllCoursesWithBookmark(7L, 100L, "difficulty");

        assertThat(result).containsExactlyElementsOf(courses);
        assertThat(conditionCaptor.getValue().mountainId()).isEqualTo(100L);
        assertThat(conditionCaptor.getValue().sortBy()).isEqualTo("difficulty");
    }

    @Test
    void 코스상세를_조회한다() {
        Course course = org.mockito.Mockito.mock(Course.class);
        given(course.getId()).willReturn(2L);
        given(course.getName()).willReturn("원효사-장불재");
        given(course.getLength()).willReturn(7.2);
        given(course.getDuration()).willReturn(240L);
        given(course.getDifficulty()).willReturn(Difficulty.NORMAL);
        given(courseRepository.findById(2L)).willReturn(Optional.of(course));

        var result = courseService.getCourseDetailsByCourseId(2L);

        assertThat(result.courseId()).isEqualTo(2L);
        assertThat(result.courseName()).isEqualTo("원효사-장불재");
        assertThat(result.courseLength()).isEqualTo(7.2);
        assertThat(result.courseDuration()).isEqualTo(240L);
        assertThat(result.difficulty()).isEqualTo(Difficulty.NORMAL);
    }

    @Test
    void 상세조회에서_없는_코스면_예외가_발생한다() {
        given(courseRepository.findById(3L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.getCourseDetailsByCourseId(3L))
                .isInstanceOf(CourseNotFoundException.class);
    }

    @Test
    void 가장_가까운_코스좌표와_인덱스를_조회한다() {
        Point userPoint = geometryFactory.createPoint(new Coordinate(126.15, 37.15));
        PathwayDTO first = PathwayDTO.builder()
                .pathwayId(1L)
                .deptBaseId(1L)
                .destBaseId(2L)
                .difficulty(Difficulty.EASY)
                .coordinates(라인을_생성한다(
                        new Coordinate(126.0, 37.0),
                        new Coordinate(126.1, 37.1)
                ))
                .build();
        PathwayDTO second = PathwayDTO.builder()
                .pathwayId(2L)
                .deptBaseId(2L)
                .destBaseId(3L)
                .difficulty(Difficulty.NORMAL)
                .coordinates(라인을_생성한다(
                        new Coordinate(126.2, 37.2),
                        new Coordinate(126.3, 37.3)
                ))
                .build();
        ClosestCoordinateInfo expected = new ClosestCoordinateInfo(new Coordinate(126.2, 37.2), 2);
        ArgumentCaptor<Coordinate[]> coordinatesCaptor = ArgumentCaptor.forClass(Coordinate[].class);
        ArgumentCaptor<Coordinate> userCoordinateCaptor = ArgumentCaptor.forClass(Coordinate.class);
        given(pathwayService.getPathwaysByCourseId(11L)).willReturn(List.of(first, second));
        given(courseLocationMatcher.findClosestCoordinateIndex(coordinatesCaptor.capture(),
                userCoordinateCaptor.capture()))
                .willReturn(expected);

        ClosestCoordinateInfo result = courseService.findClosestCoordinateWithIndex(11L, userPoint);

        assertThat(result).isEqualTo(expected);
        assertThat(coordinatesCaptor.getValue()).containsExactly(
                new Coordinate(126.0, 37.0),
                new Coordinate(126.1, 37.1),
                new Coordinate(126.2, 37.2),
                new Coordinate(126.3, 37.3)
        );
        assertThat(userCoordinateCaptor.getValue()).isEqualTo(userPoint.getCoordinate());
    }

    @Test
    void 도착여부가_1이면_true를_반환한다() {
        Point userPoint = geometryFactory.createPoint(new Coordinate(126.1, 37.1));
        given(courseRepository.isUserArrivedDestination(5L, userPoint)).willReturn(1L);

        Boolean result = courseService.isArrived(5L, userPoint);

        assertThat(result).isTrue();
    }

    @Test
    void 도착여부가_0이면_false를_반환한다() {
        Point userPoint = geometryFactory.createPoint(new Coordinate(126.1, 37.1));
        given(courseRepository.isUserArrivedDestination(5L, userPoint)).willReturn(0L);

        Boolean result = courseService.isArrived(5L, userPoint);

        assertThat(result).isFalse();
    }

    private LineString 라인을_생성한다(Coordinate... coordinates) {
        return geometryFactory.createLineString(coordinates);
    }
}
