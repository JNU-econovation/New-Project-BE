package com.econo_4factorial.newproject.course.service;

import com.econo_4factorial.newproject.course.dto.ClosestCoordinateInfo;
import com.econo_4factorial.newproject.course.exception.InternalServerException.ClosestCoordinateNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CourseLocationMatcherTest {

    private CourseLocationMatcher courseLocationMatcher;

    @BeforeEach
    void setUp() {
        courseLocationMatcher = new CourseLocationMatcher();
    }

    @Test
    void 사용자와_가장_가까운_좌표의_인덱스를_반환한다() {
        Coordinate[] coordinatesOfCourse = {
                new Coordinate(0.0, 0.0),
                new Coordinate(1.0, 1.0),
                new Coordinate(2.0, 2.0)
        };

        ClosestCoordinateInfo result = courseLocationMatcher.findClosestCoordinateIndex(
                coordinatesOfCourse,
                new Coordinate(1.1, 1.1)
        );

        assertThat(result.index()).isEqualTo(1);
        assertThat(result.coordinate().getX()).isEqualTo(1.0);
        assertThat(result.coordinate().getY()).isEqualTo(1.0);
    }

    @Test
    void 코스_좌표가_비어있으면_예외가_발생한다() {
        assertThatThrownBy(() -> courseLocationMatcher.findClosestCoordinateIndex(
                new Coordinate[]{},
                new Coordinate(1.0, 1.0)
        )).isInstanceOf(ClosestCoordinateNotFoundException.class);
    }
}
