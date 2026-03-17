package com.econo_4factorial.newproject.course.controller;

import com.econo_4factorial.newproject.common.annotation.UserId;
import com.econo_4factorial.newproject.common.constant.Difficulty;
import com.econo_4factorial.newproject.common.exception.GlobalExceptionHandler;
import com.econo_4factorial.newproject.common.exception.ValidationMessage;
import com.econo_4factorial.newproject.common.util.RedirectUriBuilder;
import com.econo_4factorial.newproject.course.dto.CourseWithBookmarkDTO;
import com.econo_4factorial.newproject.course.exception.BadRequestException.BookmarkNotFoundException;
import com.econo_4factorial.newproject.course.service.BookmarkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookmarkControllerTest {

    @Mock
    private BookmarkService bookmarkService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        BookmarkController controller = new BookmarkController(bookmarkService);
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler(new RedirectUriBuilder());
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(globalExceptionHandler)
                .setCustomArgumentResolvers(new UserIdArgumentResolver())
                .setValidator(validator)
                .build();
    }

    @Test
    void 즐겨찾기를_추가한다() throws Exception {
        mockMvc.perform(post("/api/v1/bookmarks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "courseId": 3
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(bookmarkService).addBookmark(1L, 3L);
    }

    @Test
    void 잘못된_요청으로_즐겨찾기를_추가하면_예외응답을_반환한다() throws Exception {
        mockMvc.perform(post("/api/v1/bookmarks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.errorCode").value("COMMON400_001"))
                .andExpect(jsonPath("$.message").value(ValidationMessage.COURSE_ID_REQUIRED));
    }

    @Test
    void 즐겨찾기를_삭제한다() throws Exception {
        mockMvc.perform(delete("/api/v1/bookmarks/3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(bookmarkService).deleteBookmark(1L, 3L);
    }

    @Test
    void 없는_즐겨찾기를_삭제하면_예외응답을_반환한다() throws Exception {
        doThrow(new BookmarkNotFoundException()).when(bookmarkService).deleteBookmark(1L, 3L);

        mockMvc.perform(delete("/api/v1/bookmarks/3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.errorCode").value("BOOKMARK404_001"))
                .andExpect(jsonPath("$.message").value("코스 북마크를 찾을 수 없습니다"));
    }

    @Test
    void 즐겨찾기_목록을_조회한다() throws Exception {
        List<CourseWithBookmarkDTO> bookmarks = List.of(
                new CourseWithBookmarkDTO(
                        4L,
                        "mudeung-easy",
                        "무등산 쉬운 코스",
                        20L,
                        2.1,
                        90L,
                        Difficulty.EASY,
                        true,
                        "/bookmark.png",
                        9L
                )
        );
        given(bookmarkService.getBookmarkList(1L)).willReturn(bookmarks);

        mockMvc.perform(get("/api/v1/bookmarks")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.bookmarkList.length()").value(1))
                .andExpect(jsonPath("$.data.bookmarkList[0].id").value(4))
                .andExpect(jsonPath("$.data.bookmarkList[0].displayName").value("무등산 쉬운 코스"))
                .andExpect(jsonPath("$.data.bookmarkList[0].bookmark").value(true));

        verify(bookmarkService).getBookmarkList(1L);
    }

    private static class UserIdArgumentResolver implements HandlerMethodArgumentResolver {
        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return parameter.hasParameterAnnotation(UserId.class);
        }

        @Override
        public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
            return 1L;
        }
    }
}
