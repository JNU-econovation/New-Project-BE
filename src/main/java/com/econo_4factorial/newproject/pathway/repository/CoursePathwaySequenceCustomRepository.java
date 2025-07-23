package com.econo_4factorial.newproject.pathway.repository;

import com.econo_4factorial.newproject.pathway.domain.CoursePathwaySequence;

import java.util.List;

public interface CoursePathwaySequenceCustomRepository {
    List<CoursePathwaySequence> findByCourseIdOrderBySequence(Long courseId);
}
