package com.econo_4factorial.newproject.course.service;

import com.econo_4factorial.newproject.course.domain.Course;
import com.econo_4factorial.newproject.course.repository.BookmarkRepository;
import com.econo_4factorial.newproject.user.domain.User;
import com.econo_4factorial.newproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.econo_4factorial.newproject.course.mapper.BookmarkMapper.toEntity;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final UserService userService;
    private final CourseService courseService;

    @Transactional
    public void addBookmark(Long userId, Long courseId) {
        User user = userService.findUserByIdOrThrow(userId);
        Course course = courseService.findByIdOrThrow(courseId);
        bookmarkRepository.save(toEntity(user, course));
    }
}
