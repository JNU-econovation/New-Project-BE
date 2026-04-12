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
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
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
        given(bookmarkRepository.existsByUserIdAndCourseId(1L, 2L)).willReturn(false);
        given(userService.findUserByIdOrThrow(1L)).willReturn(user);
        given(courseService.findByIdOrThrow(2L)).willReturn(course);

        bookmarkService.addBookmark(1L, 2L);

        verify(bookmarkRepository).saveAndFlush(captor.capture());
        assertThat(captor.getValue().getUser()).isEqualTo(user);
        assertThat(captor.getValue().getCourse()).isEqualTo(course);
    }

    @Test
    void 이미_같은_북마크가_존재하면_중복_저장하지_않는다() {
        given(bookmarkRepository.existsByUserIdAndCourseId(1L, 2L)).willReturn(true);

        bookmarkService.addBookmark(1L, 2L);

        verify(bookmarkRepository).existsByUserIdAndCourseId(1L, 2L);
        verify(userService, never()).findUserByIdOrThrow(any(Long.class));
        verify(courseService, never()).findByIdOrThrow(any(Long.class));
        verify(bookmarkRepository, never()).saveAndFlush(any(Bookmark.class));
    }

    @Test
    void 동시_요청으로_북마크_유니크_제약에_걸리면_멱등하게_성공_처리한다() {
        User user = org.mockito.Mockito.mock(User.class);
        Course course = org.mockito.Mockito.mock(Course.class);
        DataIntegrityViolationException duplicateBookmarkException = new DataIntegrityViolationException(
                "duplicate bookmark",
                new ConstraintViolationException(
                        "duplicate bookmark",
                        new SQLException("duplicate entry"),
                        "uk_bookmark_user_course"
                )
        );
        given(bookmarkRepository.existsByUserIdAndCourseId(1L, 2L)).willReturn(false);
        given(userService.findUserByIdOrThrow(1L)).willReturn(user);
        given(courseService.findByIdOrThrow(2L)).willReturn(course);
        given(bookmarkRepository.saveAndFlush(any(Bookmark.class))).willThrow(duplicateBookmarkException);

        assertThatCode(() -> bookmarkService.addBookmark(1L, 2L))
                .doesNotThrowAnyException();
    }

    @Test
    void 북마크_중복이_아닌_무결성_예외는_그대로_전파한다() {
        User user = org.mockito.Mockito.mock(User.class);
        Course course = org.mockito.Mockito.mock(Course.class);
        DataIntegrityViolationException foreignKeyException = new DataIntegrityViolationException(
                "foreign key constraint fails"
        );
        given(bookmarkRepository.existsByUserIdAndCourseId(1L, 2L)).willReturn(false);
        given(userService.findUserByIdOrThrow(1L)).willReturn(user);
        given(courseService.findByIdOrThrow(2L)).willReturn(course);
        given(bookmarkRepository.saveAndFlush(any(Bookmark.class))).willThrow(foreignKeyException);

        assertThatThrownBy(() -> bookmarkService.addBookmark(1L, 2L))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("foreign key");
    }

    @Test
    void 북마크를_삭제한다() {
        Bookmark bookmark = org.mockito.Mockito.mock(Bookmark.class);
        given(bookmarkRepository.findAllByUserIdAndCourseId(1L, 2L)).willReturn(List.of(bookmark));

        bookmarkService.deleteBookmark(1L, 2L);

        verify(bookmarkRepository).deleteAll(List.of(bookmark));
    }

    @Test
    void 삭제할_북마크가_없으면_예외가_발생한다() {
        given(bookmarkRepository.findAllByUserIdAndCourseId(1L, 2L)).willReturn(List.of());

        assertThatThrownBy(() -> bookmarkService.deleteBookmark(1L, 2L))
                .isInstanceOf(BookmarkNotFoundException.class);
    }

    @Test
    void 중복된_북마크_데이터가_있어도_삭제_요청은_500없이_처리한다() {
        Bookmark firstBookmark = org.mockito.Mockito.mock(Bookmark.class);
        Bookmark secondBookmark = org.mockito.Mockito.mock(Bookmark.class);
        given(bookmarkRepository.findAllByUserIdAndCourseId(1L, 2L))
                .willReturn(List.of(firstBookmark, secondBookmark));

        assertThatCode(() -> bookmarkService.deleteBookmark(1L, 2L))
                .doesNotThrowAnyException();
        verify(bookmarkRepository).deleteAll(List.of(firstBookmark, secondBookmark));
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

    @Test
    void 같은_코스를_가리키는_중복_북마크가_있어도_목록은_중복없이_반환한다() {
        Course course = org.mockito.Mockito.mock(Course.class);
        Base peakBase = org.mockito.Mockito.mock(Base.class);
        Mountain mountain = org.mockito.Mockito.mock(Mountain.class);
        Bookmark firstBookmark = Bookmark.builder()
                .user(org.mockito.Mockito.mock(User.class))
                .course(course)
                .build();
        Bookmark secondBookmark = Bookmark.builder()
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
        given(bookmarkRepository.findByUserId(1L)).willReturn(List.of(firstBookmark, secondBookmark));

        List<CourseWithBookmarkDTO> result = bookmarkService.getBookmarkList(1L);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().id()).isEqualTo(10L);
    }
}
