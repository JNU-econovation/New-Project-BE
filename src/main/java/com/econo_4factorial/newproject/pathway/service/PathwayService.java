package com.econo_4factorial.newproject.pathway.service;

import com.econo_4factorial.newproject.pathway.domain.CoursePathwaySequence;
import com.econo_4factorial.newproject.pathway.dto.PathwayDTO;
import com.econo_4factorial.newproject.pathway.mapper.PathwayMapper;
import com.econo_4factorial.newproject.pathway.repository.PathwayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PathwayService {
    private final PathwayRepository pathwayRepository;
    private final CoursePathwaySequenceService coursePathwaySequenceService;
    private final PathwayMapper pathwayMapper;

    @Transactional(readOnly = true)
    public List<PathwayDTO> getPathwaysByCourseId(Long courseId) {
        List<CoursePathwaySequence> sequences = coursePathwaySequenceService.findByCourseIdWithPathwayAndBase(courseId);
        return makePathways(sequences);
    }

    private List<PathwayDTO> makePathways(List<CoursePathwaySequence> sequences) {
        return sequences
                .stream()
                .map(sequence -> pathwayMapper.toDTO(sequence.getPathway()))
                .toList();
    }
}
