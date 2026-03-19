package com.econo_4factorial.newproject.course.controller;

import com.econo_4factorial.newproject.base.dto.CourseDetailDTO;
import com.econo_4factorial.newproject.common.annotation.UserId;
import com.econo_4factorial.newproject.common.constant.Difficulty;
import com.econo_4factorial.newproject.common.exception.GlobalExceptionHandler;
import com.econo_4factorial.newproject.common.util.RedirectUriBuilder;
import com.econo_4factorial.newproject.course.dto.CourseWithBookmarkDTO;
import com.econo_4factorial.newproject.course.exception.BadRequestException.CourseNotFoundException;
import com.econo_4factorial.newproject.course.service.CourseService;
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
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

    @Mock
    private CourseService courseService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        CourseController controller = new CourseController(courseService);
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
    void 선택한_산의_코스목록을_조회한다() throws Exception {
        List<CourseWithBookmarkDTO> courses = List.of(
                new CourseWithBookmarkDTO(
                        1L,
                        "mudeung-normal",
                        "무등산 보통 코스",
                        10L,
                        4.3,
                        150L,
                        Difficulty.NORMAL,
                        true,
                        "/course.png",
                        7L
                )
        );
        given(courseService.getAllCoursesWithBookmark(1L, 7L, "length")).willReturn(courses);

        mockMvc.perform(get("/api/v1/mountains/7/courses")
                        .param("sortBy", "length")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.courses.length()").value(1))
                .andExpect(jsonPath("$.data.courses[0].id").value(1))
                .andExpect(jsonPath("$.data.courses[0].displayName").value("무등산 보통 코스"))
                .andExpect(jsonPath("$.data.courses[0].bookmark").value(true))
                .andExpect(jsonPath("$.data.courses[0].mountainId").value(7));

        verify(courseService).getAllCoursesWithBookmark(1L, 7L, "length");
    }

    @Test
    void 코스_상세를_조회한다() throws Exception {
        CourseDetailDTO courseDetail = new CourseDetailDTO(
                11L,
                "원효봉 코스",
                5.4,
                180L,
                Difficulty.HARD
        );
        given(courseService.getCourseDetailsByCourseId(11L)).willReturn(courseDetail);

        mockMvc.perform(get("/api/v1/courses/11/details")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.courseDetail.courseId").value(11))
                .andExpect(jsonPath("$.data.courseDetail.courseName").value("원효봉 코스"))
                .andExpect(jsonPath("$.data.courseDetail.difficulty").value("HARD"));

        verify(courseService).getCourseDetailsByCourseId(11L);
    }

    @Test
    void 없는_코스를_상세조회하면_예외응답을_반환한다() throws Exception {
        given(courseService.getCourseDetailsByCourseId(99L)).willThrow(new CourseNotFoundException());

        mockMvc.perform(get("/api/v1/courses/99/details")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.errorCode").value("COURSE404_001"))
                .andExpect(jsonPath("$.message").value("코스를 찾을 수 없습니다"));
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
