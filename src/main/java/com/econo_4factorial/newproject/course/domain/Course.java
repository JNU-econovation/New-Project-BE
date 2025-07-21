package com.econo_4factorial.newproject.course.domain;

import com.econo_4factorial.newproject.common.constant.Difficulty;
import com.econo_4factorial.newproject.mountain.domain.Mountain;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mountain_id")
    private Mountain mountain;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Double length;

    @Column(nullable = false)
    private Long duration;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;
}
