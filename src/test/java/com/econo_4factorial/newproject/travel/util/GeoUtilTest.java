package com.econo_4factorial.newproject.travel.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

class GeoUtilTest {

    @Test
    void 배열_좌표를_Point로_변환한다() {
        Point point = GeoUtil.toPoint(new double[]{126.9780, 37.5665});

        assertThat(point.getX()).isEqualTo(126.9780);
        assertThat(point.getY()).isEqualTo(37.5665);
        assertThat(point.getSRID()).isEqualTo(4326);
    }

    @Test
    void Coordinate를_Point로_변환한다() {
        Point point = GeoUtil.toPoint(new Coordinate(127.0276, 37.4979));

        assertThat(point.getX()).isEqualTo(127.0276);
        assertThat(point.getY()).isEqualTo(37.4979);
        assertThat(point.getSRID()).isEqualTo(4326);
    }

    @Test
    void Point_리스트를_LineString으로_변환한다() {
        List<Point> points = List.of(
                GeoUtil.toPoint(new Coordinate(126.9780, 37.5665)),
                GeoUtil.toPoint(new Coordinate(127.0276, 37.4979))
        );

        LineString lineString = GeoUtil.toLineString(points);

        assertThat(lineString.getNumPoints()).isEqualTo(2);
        assertThat(lineString.getCoordinateN(0).getX()).isEqualTo(126.9780);
        assertThat(lineString.getCoordinateN(0).getY()).isEqualTo(37.5665);
        assertThat(lineString.getCoordinateN(1).getX()).isEqualTo(127.0276);
        assertThat(lineString.getCoordinateN(1).getY()).isEqualTo(37.4979);
        assertThat(lineString.getSRID()).isEqualTo(4326);
    }
}
