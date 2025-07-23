package com.econo_4factorial.newproject.pathway.domain;


import com.econo_4factorial.newproject.course.domain.Course;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CoursePathwaySequence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pathway_id", nullable = false)
    private Pathway pathway;

    @Column(nullable = false)
    private Long sequence;
}
