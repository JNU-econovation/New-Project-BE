package com.econo_4factorial.newproject.pathway.domain;

import com.econo_4factorial.newproject.base.domain.Base;
import com.econo_4factorial.newproject.common.constant.Difficulty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pathway {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_id", nullable = false)
    private Base departure;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id", nullable = false)
    private Base destination;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String Coordinates;

    @Column(nullable = false)
    private Double length;

    @Column(nullable = false)
    private Long duration;

    @Column(nullable = false)
    private Difficulty difficulty;
}
