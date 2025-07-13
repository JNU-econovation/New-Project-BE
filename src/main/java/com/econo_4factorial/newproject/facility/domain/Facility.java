package com.econo_4factorial.newproject.facility.domain;

import com.econo_4factorial.newproject.mountain.domain.Mountain;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Facility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mountain_id")
    private Mountain mountain;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FacilityType type;

    @Column(precision = 16, scale = 14, nullable = false)
    private BigDecimal latitude;

    @Column(precision = 17, scale = 14, nullable = false)
    private BigDecimal longitude;
}
