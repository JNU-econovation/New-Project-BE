package com.econo_4factorial.newproject.pathway.service;

import com.econo_4factorial.newproject.pathway.domain.CoursePathwaySequence;
import com.econo_4factorial.newproject.pathway.repository.CoursePathwaySequenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CoursePathwaySequenceServiceTest {

    @Mock
    private CoursePathwaySequenceRepository coursePathwaySequenceRepository;

    private CoursePathwaySequenceService coursePathwaySequenceService;

    @BeforeEach
    void setUp() {
        coursePathwaySequenceService = new CoursePathwaySequenceService(coursePathwaySequenceRepository);
    }

    @Test
    void 코스_ID로_정렬된_sequence_목록을_조회한다() {
        List<CoursePathwaySequence> sequences = List.of(
                org.mockito.Mockito.mock(CoursePathwaySequence.class),
                org.mockito.Mockito.mock(CoursePathwaySequence.class)
        );
        given(coursePathwaySequenceRepository.findByCourseIdOrderBySequence(7L)).willReturn(sequences);

        List<CoursePathwaySequence> result = coursePathwaySequenceService.findByCourseId(7L);

        assertThat(result).containsExactlyElementsOf(sequences);
        verify(coursePathwaySequenceRepository).findByCourseIdOrderBySequence(7L);
    }
}
