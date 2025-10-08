package com.econo_4factorial.newproject.travel.domain;

import com.econo_4factorial.newproject.course.domain.Course;
import com.econo_4factorial.newproject.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.LineString;

import java.time.Duration;
import java.time.LocalDateTime;

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

    @Builder
    public TravelRecord(User user, Course course, LineString paths, LocalDateTime startedAt, LocalDateTime endAt, double totalTravelDistanceKm, Duration totalTravelTime) {
        this.user = user;
        this.course = course;
        this.paths = paths;
        this.startedAt = startedAt;
        this.endAt = endAt;
        this.totalTravelDistanceKm = totalTravelDistanceKm;
        this.totalTravelTime = totalTravelTime;
    }
}
