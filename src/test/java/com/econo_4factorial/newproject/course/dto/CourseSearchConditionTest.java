package com.econo_4factorial.newproject.course.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CourseSearchConditionTest {

    @Test
    void 산과_정렬조건으로_검색조건을_생성한다() {
        CourseSearchCondition result = CourseSearchCondition.of(3L, "difficulty");

        assertThat(result.mountainId()).isEqualTo(3L);
        assertThat(result.sortBy()).isEqualTo("difficulty");
    }

    @Test
    void null_값으로도_검색조건을_생성한다() {
        CourseSearchCondition result = CourseSearchCondition.of(null, null);

        assertThat(result.mountainId()).isNull();
        assertThat(result.sortBy()).isNull();
    }
}
