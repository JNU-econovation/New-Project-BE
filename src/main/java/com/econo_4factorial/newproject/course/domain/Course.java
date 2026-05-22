package com.econo_4factorial.newproject.course.domain;

import com.econo_4factorial.newproject.base.domain.Base;
import com.econo_4factorial.newproject.common.constant.Difficulty;
import com.econo_4factorial.newproject.mountain.domain.Mountain;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.LineString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mountain_id", nullable = false)
    private Mountain mountain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "peak_base_id", nullable = false)
    private Base peakBase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_base_id", nullable = false)
    private Base destinationBase;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Double length;

    @Column(nullable = false)
    private Long duration;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String displayName;

    @Column(columnDefinition = "LINESTRING SRID 4326")
    private LineString coordinates;
}
