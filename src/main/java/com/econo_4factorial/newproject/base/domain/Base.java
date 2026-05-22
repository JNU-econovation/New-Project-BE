package com.econo_4factorial.newproject.base.domain;

import com.econo_4factorial.newproject.mountain.domain.Mountain;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"geo_point"})
        }
)
public class Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mountain_id")
    private Mountain mountain;

    @Column(unique = true, nullable = false)
    private String name;

    private String weather;

    private Double temperature;

    @Column(name = "geo_point", columnDefinition = "POINT SRID 4326", nullable = false)
    private Point GeoPoint;

    @Column(nullable = false)
    private Long altitude;

    public void updateWeather(String weather, Double temperature) {
        this.weather = weather;
        this.temperature = temperature;
    }
}
