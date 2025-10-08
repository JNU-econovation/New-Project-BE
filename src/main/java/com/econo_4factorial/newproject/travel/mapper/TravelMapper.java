package com.econo_4factorial.newproject.travel.mapper;

import com.econo_4factorial.newproject.course.domain.Course;
import com.econo_4factorial.newproject.travel.domain.TravelRecord;
import com.econo_4factorial.newproject.travel.domain.TravelTrackingInfo;
import com.econo_4factorial.newproject.travel.domain.vo.RemainingTime;
import com.econo_4factorial.newproject.travel.util.GeoUtil;
import com.econo_4factorial.newproject.user.domain.User;
import lombok.experimental.UtilityClass;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public final class TravelMapper {
    public static TravelTrackingInfo toDefaultInfo(Long userId, Long courseId, LocalDateTime startedAt, Point userPoint) {
        List<Point> paths = new ArrayList<>();
        paths.add(userPoint);

        return TravelTrackingInfo.builder()
                .userId(userId)
                .courseId(courseId)
                .startedAt(startedAt)
                .paths(paths)
                .build();
    }

    public static TravelRecord toRecord(User user, Course course, TravelTrackingInfo info) {
        return TravelRecord.builder()
                .user(user)
                .course(course)
                .paths(GeoUtil.toLineString(info.getPaths()))
                .startedAt(info.getStartedAt())
                .endAt(info.getEndAt())
                .totalTravelDistanceKm(info.getTotalTravelDistanceKm())
                .totalTravelTime(info.getTotalTravelTime())
                .build();

    }
}
