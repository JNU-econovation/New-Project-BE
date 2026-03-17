package com.econo_4factorial.newproject.course.service;

import com.econo_4factorial.newproject.base.domain.Base;
import com.econo_4factorial.newproject.common.constant.Difficulty;
import com.econo_4factorial.newproject.course.domain.Bookmark;
import com.econo_4factorial.newproject.course.domain.Course;
import com.econo_4factorial.newproject.course.dto.CourseWithBookmarkDTO;
import com.econo_4factorial.newproject.course.exception.BadRequestException.BookmarkNotFoundException;
import com.econo_4factorial.newproject.course.repository.BookmarkRepository;
import com.econo_4factorial.newproject.mountain.domain.Mountain;
import com.econo_4factorial.newproject.user.domain.User;
import com.econo_4factorial.newproject.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

    @Mock
    private BookmarkRepository bookmarkRepository;

    @Mock
    private UserService userService;

    @Mock
    private CourseService courseService;

    private BookmarkService bookmarkService;

    @BeforeEach
    void setUp() {
        bookmarkService = new BookmarkService(bookmarkRepository, userService, courseService);
    }

    @Test
    void 북마크를_추가한다() {
        User user = org.mockito.Mockito.mock(User.class);
        Course course = org.mockito.Mockito.mock(Course.class);
        ArgumentCaptor<Bookmark> captor = ArgumentCaptor.forClass(Bookmark.class);
        given(userService.findUserByIdOrThrow(1L)).willReturn(user);
        given(courseService.findByIdOrThrow(2L)).willReturn(course);

        bookmarkService.addBookmark(1L, 2L);

        verify(bookmarkRepository).save(captor.capture());
        assertThat(captor.getValue().getUser()).isEqualTo(user);
        assertThat(captor.getValue().getCourse()).isEqualTo(course);
    }

    @Test
    void 북마크를_삭제한다() {
        Bookmark bookmark = org.mockito.Mockito.mock(Bookmark.class);
        given(bookmarkRepository.findByUserIdAndCourseId(1L, 2L)).willReturn(Optional.of(bookmark));

        bookmarkService.deleteBookmark(1L, 2L);

        verify(bookmarkRepository).delete(bookmark);
    }

    @Test
    void 삭제할_북마크가_없으면_예외가_발생한다() {
        given(bookmarkRepository.findByUserIdAndCourseId(1L, 2L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> bookmarkService.deleteBookmark(1L, 2L))
                .isInstanceOf(BookmarkNotFoundException.class);
    }

    @Test
    void 북마크목록을_조회한다() {
        Course course = org.mockito.Mockito.mock(Course.class);
        Base peakBase = org.mockito.Mockito.mock(Base.class);
        Mountain mountain = org.mockito.Mockito.mock(Mountain.class);
        Bookmark bookmark = Bookmark.builder()
                .user(org.mockito.Mockito.mock(User.class))
                .course(course)
                .build();
        given(course.getId()).willReturn(10L);
        given(course.getName()).willReturn("course-10");
        given(course.getDisplayName()).willReturn("중머리재 코스");
        given(course.getPeakBase()).willReturn(peakBase);
        given(peakBase.getId()).willReturn(20L);
        given(course.getLength()).willReturn(3.5);
        given(course.getDuration()).willReturn(120L);
        given(course.getDifficulty()).willReturn(Difficulty.EASY);
        given(course.getImageUrl()).willReturn("/course-10.png");
        given(course.getMountain()).willReturn(mountain);
        given(mountain.getId()).willReturn(30L);
        given(bookmarkRepository.findByUserId(1L)).willReturn(List.of(bookmark));

        List<CourseWithBookmarkDTO> result = bookmarkService.getBookmarkList(1L);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().id()).isEqualTo(10L);
        assertThat(result.getFirst().bookmark()).isTrue();
        assertThat(result.getFirst().displayName()).isEqualTo("중머리재 코스");
    }
}
