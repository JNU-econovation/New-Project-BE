package com.econo_4factorial.newproject.course.service;

import com.econo_4factorial.newproject.course.domain.Bookmark;
import com.econo_4factorial.newproject.course.domain.Course;
import com.econo_4factorial.newproject.course.dto.CourseWithBookmarkDTO;
import com.econo_4factorial.newproject.course.exception.BadRequestException.BookmarkNotFoundException;
import com.econo_4factorial.newproject.course.repository.BookmarkRepository;
import com.econo_4factorial.newproject.user.domain.User;
import com.econo_4factorial.newproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.econo_4factorial.newproject.course.mapper.BookmarkMapper.toEntity;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookmarkService {
    private static final String BOOKMARK_UNIQUE_CONSTRAINT = "uk_bookmark_user_course";

    private final Boolean TRUE = true;

    private final BookmarkRepository bookmarkRepository;
    private final UserService userService;
    private final CourseService courseService;

    @Transactional
    public void addBookmark(Long userId, Long courseId) {
        if (bookmarkRepository.existsByUserIdAndCourseId(userId, courseId)) {
            return;
        }

        User user = userService.findUserByIdOrThrow(userId);
        Course course = courseService.findByIdOrThrow(courseId);

        try {
            bookmarkRepository.saveAndFlush(toEntity(user, course));
        } catch (DataIntegrityViolationException e) {
            if (!isDuplicateBookmarkConstraintViolation(e)) {
                throw e;
            }
            log.info("Bookmark already exists due to concurrent request. userId={}, courseId={}", userId, courseId);
        }
    }

    @Transactional
    public void deleteBookmark(Long userId, Long courseId) {
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUserIdAndCourseId(userId, courseId);
        if (bookmarks.isEmpty()) {
            throw new BookmarkNotFoundException();
        }
        bookmarkRepository.deleteAll(bookmarks);
    }

    @Transactional(readOnly = true)
    public List<CourseWithBookmarkDTO> getBookmarkList(Long userId) {
        return bookmarkRepository.findByUserId(userId)
                .stream()
                .collect(Collectors.toMap(
                        bookmark -> bookmark.getCourse().getId(),
                        bookmark -> CourseWithBookmarkDTO.from(bookmark.getCourse(), TRUE),
                        (first, ignored) -> first,
                        LinkedHashMap::new
                ))
                .values()
                .stream()
                .toList();
    }

    private boolean isDuplicateBookmarkConstraintViolation(Throwable throwable) {
        Throwable cause = throwable;

        while (cause != null) {
            if (cause instanceof ConstraintViolationException constraintViolationException
                    && BOOKMARK_UNIQUE_CONSTRAINT.equals(constraintViolationException.getConstraintName())) {
                return true;
            }

            String message = cause.getMessage();
            if (message != null && message.contains(BOOKMARK_UNIQUE_CONSTRAINT)) {
                return true;
            }

            cause = cause.getCause();
        }

        return false;
    }
}
