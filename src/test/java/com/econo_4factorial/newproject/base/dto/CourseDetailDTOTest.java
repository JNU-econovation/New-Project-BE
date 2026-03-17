package com.econo_4factorial.newproject.base.dto;

import com.econo_4factorial.newproject.common.constant.Difficulty;
import com.econo_4factorial.newproject.course.domain.Course;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CourseDetailDTOTest {

    @Test
    void 코스를_상세_DTO로_변환한다() {
        Course course = mock(Course.class);
        when(course.getId()).thenReturn(6L);
        when(course.getName()).thenReturn("장불재 코스");
        when(course.getLength()).thenReturn(6.7);
        when(course.getDuration()).thenReturn(210L);
        when(course.getDifficulty()).thenReturn(Difficulty.NORMAL);

        CourseDetailDTO result = CourseDetailDTO.from(course);

        assertThat(result.courseId()).isEqualTo(6L);
        assertThat(result.courseName()).isEqualTo("장불재 코스");
        assertThat(result.courseLength()).isEqualTo(6.7);
        assertThat(result.courseDuration()).isEqualTo(210L);
        assertThat(result.difficulty()).isEqualTo(Difficulty.NORMAL);
    }
}
