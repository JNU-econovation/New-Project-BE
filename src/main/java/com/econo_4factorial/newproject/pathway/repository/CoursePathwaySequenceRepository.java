package com.econo_4factorial.newproject.pathway.repository;

import com.econo_4factorial.newproject.pathway.domain.CoursePathwaySequence;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoursePathwaySequenceRepository extends JpaRepository<CoursePathwaySequence, Long> {
    
    @EntityGraph(attributePaths = {"pathway", "pathway.departure", "pathway.destination"})
    List<CoursePathwaySequence> findByCourseIdOrderBySequence(Long courseId);
}
