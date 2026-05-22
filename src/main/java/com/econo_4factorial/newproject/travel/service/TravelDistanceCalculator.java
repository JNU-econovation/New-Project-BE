package com.econo_4factorial.newproject.travel.service;

import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.linearref.LengthIndexedLine;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TravelDistanceCalculator {
    private final Long EARTH_RADIUS_M = 6371000L;
    private final Double ZERO = 0.0;

    public Double calculateDistanceFromLastLocation(Point prevPoint, Point currentPoint) {
        if (prevPoint == null) {
            return ZERO;
        }

        double lat1 = prevPoint.getY();
        double lon1 = prevPoint.getX();
        double lat2 = currentPoint.getY();
        double lon2 = currentPoint.getX();

        return haversineKm(lat1, lon1, lat2, lon2);
    }

    private double haversineKm(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_M * c / 1000; //km 단위로 변환
    }

    public double calculateDistanceBetweenPoints(Point closestPoint, Point userPoint) {
        double lat1 = closestPoint.getY();
        double lon1 = closestPoint.getX();
        double lat2 = userPoint.getY();
        double lon2 = userPoint.getX();

        return haversine(lat1, lon1, lat2, lon2);
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public Double calculateRemainingDistanceToPointInCourse(LineString lineStringOfCourse, Point targetPoint,
                                                            Point userPoint) {
        LengthIndexedLine indexedLine = new LengthIndexedLine(lineStringOfCourse);

        double userIndex = indexedLine.indexOf(userPoint.getCoordinate());
        double peakIndex = indexedLine.indexOf(targetPoint.getCoordinate());

        if (userIndex > peakIndex) {
            return ZERO;
        }

        Geometry remainingLine = indexedLine.extractLine(userIndex, peakIndex);

        return sumDistanceOfRemainingLine(remainingLine);
    }

    private Double sumDistanceOfRemainingLine(Geometry remainingLine) {
        Coordinate[] coordinates = remainingLine.getCoordinates();
        return IntStream.range(0, coordinates.length - 1)
                .mapToDouble(i -> haversine(
                        coordinates[i].y, coordinates[i].x,
                        coordinates[i + 1].y, coordinates[i + 1].x
                ))
                .sum();
    }
}
