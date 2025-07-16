package com.econo_4factorial.newproject.course.mapper;

import com.econo_4factorial.newproject.course.domain.Bookmark;
import com.econo_4factorial.newproject.course.domain.Course;
import com.econo_4factorial.newproject.user.domain.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookmarkMapper {
    public static Bookmark toEntity (User user, Course course) {
        return Bookmark.builder()
                .user(user)
                .course(course)
                .build();
    }
}
