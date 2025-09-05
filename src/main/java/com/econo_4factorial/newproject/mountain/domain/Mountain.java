package com.econo_4factorial.newproject.mountain.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mountain{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String location;

    @Column(precision = 16, scale = 14)
    private BigDecimal latitude;

    @Column(precision = 17, scale = 14)
    private BigDecimal longitude;
}
