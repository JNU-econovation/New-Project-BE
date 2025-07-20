package com.econo_4factorial.newproject.course.service;

import com.econo_4factorial.newproject.course.domain.Bookmark;
import com.econo_4factorial.newproject.course.domain.Course;
import com.econo_4factorial.newproject.course.dto.CourseWithBookmarkDTO;
import com.econo_4factorial.newproject.course.exception.BadRequestException.BookmarkNotFoundException;
import com.econo_4factorial.newproject.course.repository.BookmarkRepository;
import com.econo_4factorial.newproject.user.domain.User;
import com.econo_4factorial.newproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.econo_4factorial.newproject.course.mapper.BookmarkMapper.toEntity;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final Boolean TRUE = true;

    private final BookmarkRepository bookmarkRepository;
    private final UserService userService;
    private final CourseService courseService;

    @Transactional
    public void addBookmark(Long userId, Long courseId) {
        User user = userService.findUserByIdOrThrow(userId);
        Course course = courseService.findByIdOrThrow(courseId);
        bookmarkRepository.save(toEntity(user, course));
    }

    @Transactional
    public void deleteBookmark(Long userId, Long courseId) {
        Bookmark bookmark =  bookmarkRepository.findByUserIdAndCourseId(userId, courseId)
                .orElseThrow(BookmarkNotFoundException::new);

        bookmarkRepository.delete(bookmark);
    }

    @Transactional(readOnly = true)
    public List<CourseWithBookmarkDTO> getBookmarkList(Long userId) {
        return bookmarkRepository.findByUserId(userId)
                .stream()
                .map(bookmark -> {
                    return CourseWithBookmarkDTO.from(bookmark.getCourse(), TRUE);
                })
                .toList();
    }
}
