package com.econo_4factorial.newproject.base.domain;

import com.econo_4factorial.newproject.mountain.domain.Mountain;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"latitude", "longitude"})
        }
)
public class Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mountain_id")
    private Mountain mountain;

    @Column(unique = true,nullable = false)
    private String name;

    private String weather;

    private Double temperature;

    @Column(precision = 16, scale = 14, nullable = false)
    private BigDecimal latitude;

    @Column(precision = 17, scale = 14, nullable = false)
    private BigDecimal longitude;

    @Column(nullable = false)
    private Long altitude;

    public void updateWeather(String weather, Double temperature) {
        this.weather = weather;
        this.temperature = temperature;
    }
}
