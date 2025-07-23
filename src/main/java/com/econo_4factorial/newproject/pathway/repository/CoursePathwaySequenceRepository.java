package com.econo_4factorial.newproject.pathway.repository;

import com.econo_4factorial.newproject.pathway.domain.CoursePathwaySequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoursePathwaySequenceRepository extends JpaRepository<CoursePathwaySequence, Long> {
    List<CoursePathwaySequence> findByCourseIdOrderBySequence(Long courseId);
}
