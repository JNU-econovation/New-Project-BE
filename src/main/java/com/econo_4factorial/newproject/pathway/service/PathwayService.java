package com.econo_4factorial.newproject.pathway.service;

import com.econo_4factorial.newproject.pathway.repository.CoursePathwaySequenceRepository;
import com.econo_4factorial.newproject.pathway.repository.PathwayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PathwayService {
    private final PathwayRepository pathwayRepository;
    private final CoursePathwaySequenceRepository coursePathwaySequenceRepository;


}
