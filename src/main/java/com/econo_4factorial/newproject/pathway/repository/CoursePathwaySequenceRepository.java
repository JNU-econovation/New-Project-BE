package com.econo_4factorial.newproject.pathway.repository;

import com.econo_4factorial.newproject.pathway.domain.CoursePathwaySequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoursePathwaySequenceRepository extends JpaRepository<CoursePathwaySequence, Long> {
}
