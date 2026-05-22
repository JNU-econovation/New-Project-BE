package com.econo_4factorial.newproject.travel.domain;

import com.econo_4factorial.newproject.course.domain.Course;
import com.econo_4factorial.newproject.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.LineString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(columnDefinition = "LINESTRING SRID 4326", nullable = false)
    private LineString paths;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    @Column(nullable = false)
    private LocalDateTime endAt;

    @Column(nullable = false)
    private double totalTravelDistanceKm;

    @Column(nullable = false)
    private Duration totalTravelTime;

    @Column(nullable = false)
    private String displayName;

    @Builder
    public TravelRecord(User user, Course course, LineString paths, LocalDateTime startedAt, LocalDateTime endAt,
                        double totalTravelDistanceKm, Duration totalTravelTime, String displayName) {
        this.user = user;
        this.course = course;
        this.paths = paths;
        this.startedAt = startedAt;
        this.endAt = endAt;
        this.totalTravelDistanceKm = totalTravelDistanceKm;
        this.totalTravelTime = totalTravelTime;
        this.displayName = displayName;
    }
}
