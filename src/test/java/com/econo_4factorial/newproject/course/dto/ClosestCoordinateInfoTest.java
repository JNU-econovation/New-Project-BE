package com.econo_4factorial.newproject.course.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;

class ClosestCoordinateInfoTest {

    @Test
    void 가장가까운_좌표정보를_보관한다() {
        Coordinate coordinate = new Coordinate(126.123, 37.456);

        ClosestCoordinateInfo info = new ClosestCoordinateInfo(coordinate, 4);

        assertThat(info.coordinate()).isEqualTo(coordinate);
        assertThat(info.index()).isEqualTo(4);
    }
}
