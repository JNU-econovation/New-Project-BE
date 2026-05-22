package com.econo_4factorial.newproject.travel.util;

import java.util.List;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Component;

@Component
public class GeoUtil {
    private static final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    private static final Integer WGS84 = 4326;

    public static Point toPoint(double[] coordinate) {
        Point point = geometryFactory.createPoint(new Coordinate(coordinate[0], coordinate[1]));
        point.setSRID(WGS84);
        return point;
    }

    public static Point toPoint(Coordinate coordinate) {
        Point point = geometryFactory.createPoint(coordinate);
        point.setSRID(WGS84);
        return point;
    }

    public static LineString toLineString(List<Point> points) {
        Coordinate[] coordinates = points.stream()
                .map(Point::getCoordinate)
                .toArray(Coordinate[]::new);

        return geometryFactory.createLineString(coordinates);
    }
}
