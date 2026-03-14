package com.econo_4factorial.newproject.travel.dto;

import com.econo_4factorial.newproject.travel.domain.TravelRecord;
import org.locationtech.jts.geom.LineString;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Stream;

public record TravelRecordDetailDTO (
        Long recordId,
        String displayName,
        Long startedAt,
        Long endAt,
        Long duration,
        Double length,
        List<List<BigDecimal>> coordinates,
        Long courseId
) {
    public static TravelRecordDetailDTO from(TravelRecord travelRecord) {
        Long startedAt = Timestamp.valueOf(travelRecord.getStartedAt()).getTime();
        Long endAt = Timestamp.valueOf(travelRecord.getStartedAt()).getTime();
        List<List<BigDecimal>> coordinates = convertCoordinatesFromLineString(travelRecord.getPaths());

        return new TravelRecordDetailDTO(
                travelRecord.getId(),
                travelRecord.getDisplayName(),
                startedAt,
                endAt,
                travelRecord.getTotalTravelTime().toMillis(),
                travelRecord.getTotalTravelDistanceKm(),
                coordinates,
                travelRecord.getCourse().getId()
        );
    }

    private static List<List<BigDecimal>> convertCoordinatesFromLineString(LineString coordinates) {
        return Stream.of(coordinates.getCoordinates())
                .map(coordinate -> List.of(
                        BigDecimal.valueOf(coordinate.getX()),
                        BigDecimal.valueOf(coordinate.getY())
                ))
                .toList();
    }
}
