package com.econo_4factorial.newproject.pathway.service;

import com.econo_4factorial.newproject.pathway.domain.CoursePathwaySequence;
import com.econo_4factorial.newproject.pathway.dto.PathwayDTO;
import com.econo_4factorial.newproject.pathway.mapper.PathwayMapper;
import com.econo_4factorial.newproject.pathway.repository.PathwayRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PathwayService {
    private final PathwayRepository pathwayRepository;
    private final CoursePathwaySequenceService coursePathwaySequenceService;
    private final PathwayMapper pathwayMapper;

    @Transactional(readOnly = true)
    public List<PathwayDTO> getPathwaysByCourseId(Long courseId) {
        List<CoursePathwaySequence> sequences = coursePathwaySequenceService.findByCourseId(courseId);
        return makePathways(sequences);
    }

    private List<PathwayDTO> makePathways(List<CoursePathwaySequence> sequences) {
        return sequences
                .stream()
                .map(sequence -> pathwayMapper.toDTO(sequence.getPathway()))
                .toList();
    }
}
