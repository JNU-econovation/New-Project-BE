package com.econo_4factorial.newproject.course.dto;

import com.econo_4factorial.newproject.base.domain.Base;
import com.econo_4factorial.newproject.common.constant.Difficulty;
import com.econo_4factorial.newproject.course.domain.Course;
import com.econo_4factorial.newproject.mountain.domain.Mountain;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

class CourseWithBookmarkDTOTest {

    @Test
    void 코스와_북마크여부로_DTO를_생성한다() {
        Course course = Mockito.mock(Course.class);
        Base peakBase = Mockito.mock(Base.class);
        Mountain mountain = Mockito.mock(Mountain.class);

        given(course.getId()).willReturn(1L);
        given(course.getName()).willReturn("course-name");
        given(course.getDisplayName()).willReturn("원효사 코스");
        given(course.getPeakBase()).willReturn(peakBase);
        given(peakBase.getId()).willReturn(11L);
        given(course.getLength()).willReturn(6.5);
        given(course.getDuration()).willReturn(240L);
        given(course.getDifficulty()).willReturn(Difficulty.NORMAL);
        given(course.getImageUrl()).willReturn("/course.png");
        given(course.getMountain()).willReturn(mountain);
        given(mountain.getId()).willReturn(21L);

        CourseWithBookmarkDTO result = CourseWithBookmarkDTO.from(course, true);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("course-name");
        assertThat(result.displayName()).isEqualTo("원효사 코스");
        assertThat(result.peakBaseId()).isEqualTo(11L);
        assertThat(result.length()).isEqualTo(6.5);
        assertThat(result.duration()).isEqualTo(240L);
        assertThat(result.difficulty()).isEqualTo(Difficulty.NORMAL);
        assertThat(result.bookmark()).isTrue();
        assertThat(result.image()).isEqualTo("/course.png");
        assertThat(result.mountainId()).isEqualTo(21L);
    }
}
