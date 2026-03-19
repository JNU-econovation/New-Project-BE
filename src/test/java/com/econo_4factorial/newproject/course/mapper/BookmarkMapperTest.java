package com.econo_4factorial.newproject.course.mapper;

import com.econo_4factorial.newproject.course.domain.Bookmark;
import com.econo_4factorial.newproject.course.domain.Course;
import com.econo_4factorial.newproject.user.domain.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

class BookmarkMapperTest {

    @Test
    void 사용자와_코스로_북마크를_생성한다() {
        User user = Mockito.mock(User.class);
        Course course = Mockito.mock(Course.class);

        Bookmark result = BookmarkMapper.toEntity(user, course);

        assertThat(result.getUser()).isEqualTo(user);
        assertThat(result.getCourse()).isEqualTo(course);
    }
}
