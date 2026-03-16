package com.econo_4factorial.newproject.pathway.service;

import com.econo_4factorial.newproject.pathway.domain.CoursePathwaySequence;
import com.econo_4factorial.newproject.pathway.repository.CoursePathwaySequenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoursePathwaySequenceService {
    private final CoursePathwaySequenceRepository coursePathwaySequenceRepository;

    public List<CoursePathwaySequence> findByCourseId(Long courseId) {
        return coursePathwaySequenceRepository.findByCourseIdOrderBySequence(courseId);
    }
}
