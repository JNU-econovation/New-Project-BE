package com.econo_4factorial.newproject.travel.controller;

import com.econo_4factorial.newproject.common.annotation.UserId;
import com.econo_4factorial.newproject.common.exception.GlobalExceptionHandler;
import com.econo_4factorial.newproject.common.util.RedirectUriBuilder;
import com.econo_4factorial.newproject.travel.dto.TravelRecordDTO;
import com.econo_4factorial.newproject.travel.dto.TravelRecordDetailDTO;
import com.econo_4factorial.newproject.travel.exception.TravelRecordNotFoundException;
import com.econo_4factorial.newproject.travel.service.TravelRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TravelRecordControllerTest {

    @Mock
    private TravelRecordService travelRecordService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        TravelRecordController controller = new TravelRecordController(travelRecordService);
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler(new RedirectUriBuilder());

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(globalExceptionHandler)
                .setCustomArgumentResolvers(new HandlerMethodArgumentResolver() {
                    @Override
                    public boolean supportsParameter(MethodParameter parameter) {
                        return parameter.hasParameterAnnotation(UserId.class);
                    }

                    @Override
                    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
                        return 1L;
                    }
                })
                .build();
    }

    @Test
    void 월별_산행기록을_조회한다() throws Exception {
        List<TravelRecordDTO> records = List.of(
                new TravelRecordDTO(1L, 1715318400000L, "무등산 코스", "/course.png", 4.2, 120L,
                        com.econo_4factorial.newproject.common.constant.Difficulty.NORMAL)
        );
        given(travelRecordService.findRecordByMonth(1L, 2024, 5)).willReturn(records);

        mockMvc.perform(get("/api/v1/travel/records")
                        .param("year", "2024")
                        .param("month", "5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.records.length()").value(1))
                .andExpect(jsonPath("$.data.records[0].id").value(1))
                .andExpect(jsonPath("$.data.records[0].displayName").value("무등산 코스"));

        verify(travelRecordService).findRecordByMonth(1L, 2024, 5);
    }

    @Test
    void 산행기록_상세를_조회한다() throws Exception {
        TravelRecordDetailDTO recordDetail = new TravelRecordDetailDTO(
                7L,
                "상세 코스",
                1715318400000L,
                1715325600000L,
                7200000L,
                5.3,
                List.of(
                        List.of(java.math.BigDecimal.valueOf(126.0), java.math.BigDecimal.valueOf(37.0)),
                        List.of(java.math.BigDecimal.valueOf(126.1), java.math.BigDecimal.valueOf(37.1))
                ),
                3L
        );
        given(travelRecordService.findRecordById(7L)).willReturn(recordDetail);

        mockMvc.perform(get("/api/v1/travel/records/7/details")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.recordId").value(7))
                .andExpect(jsonPath("$.data.displayName").value("상세 코스"))
                .andExpect(jsonPath("$.data.courseId").value(3))
                .andExpect(jsonPath("$.data.mountainId").value(1));

        verify(travelRecordService).findRecordById(7L);
    }

    @Test
    void 없는_산행기록을_상세조회하면_예외응답을_반환한다() throws Exception {
        given(travelRecordService.findRecordById(99L)).willThrow(new TravelRecordNotFoundException());

        mockMvc.perform(get("/api/v1/travel/records/99/details")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.errorCode").value("TRAVEL404_001"))
                .andExpect(jsonPath("$.message").value("산행 기록을 찾을 수 없습니다"));
    }

    @Test
    void 산행기록을_삭제한다() throws Exception {
        mockMvc.perform(delete("/api/v1/travel/records/11")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(travelRecordService).deleteRecordById(11L);
    }

    @Test
    void 필수_요청파라미터가_없으면_예외응답을_반환한다() throws Exception {
        mockMvc.perform(get("/api/v1/travel/records")
                        .param("year", "2024")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"));
    }
}
