package com.econo_4factorial.newproject.base.controller;

import com.econo_4factorial.newproject.base.dto.BaseDTO;
import com.econo_4factorial.newproject.base.dto.BaseDetailDTO;
import com.econo_4factorial.newproject.base.service.BaseService;
import com.econo_4factorial.newproject.common.exception.GlobalExceptionHandler;
import com.econo_4factorial.newproject.common.util.RedirectUriBuilder;
import com.econo_4factorial.newproject.mountain.exception.BadRequestException.MountainNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BaseControllerTest {

    @Mock
    private BaseService baseService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        BaseController controller = new BaseController(baseService);
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler(new RedirectUriBuilder());

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(globalExceptionHandler)
                .build();
    }

    @Test
    void 산의_베이스목록을_조회한다() throws Exception {
        List<BaseDTO> bases = List.of(
                new BaseDTO(
                        4L,
                        "중머리재",
                        List.of(new BigDecimal("126.9876"), new BigDecimal("35.1234"))
                )
        );
        given(baseService.getBasesByMountainId(7L)).willReturn(bases);

        mockMvc.perform(get("/api/v1/bases")
                        .param("mountainId", "7")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.mountainId").value(7))
                .andExpect(jsonPath("$.data.bases.length()").value(1))
                .andExpect(jsonPath("$.data.bases[0].baseId").value(4))
                .andExpect(jsonPath("$.data.bases[0].name").value("중머리재"));

        verify(baseService).getBasesByMountainId(7L);
    }

    @Test
    void 산의_베이스상세목록을_조회한다() throws Exception {
        List<BaseDetailDTO> baseDetails = List.of(
                new BaseDetailDTO(
                        11L,
                        "장불재",
                        "Rain",
                        13.0,
                        null,
                        List.of("/bases/11-1.png")
                )
        );
        given(baseService.getBaseDetailsByMountainId(8L)).willReturn(baseDetails);

        mockMvc.perform(get("/api/v1/bases/8/details")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.mountainId").value(8))
                .andExpect(jsonPath("$.data.baseDetails.length()").value(1))
                .andExpect(jsonPath("$.data.baseDetails[0].baseId").value(11))
                .andExpect(jsonPath("$.data.baseDetails[0].weather").value("Rain"))
                .andExpect(jsonPath("$.data.baseDetails[0].images[0]").value("/bases/11-1.png"));

        verify(baseService).getBaseDetailsByMountainId(8L);
    }

    @Test
    void 없는_산의_베이스상세를_조회하면_예외응답을_반환한다() throws Exception {
        given(baseService.getBaseDetailsByMountainId(99L)).willThrow(new MountainNotFoundException());

        mockMvc.perform(get("/api/v1/bases/99/details")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.errorCode").value("MOUNTAIN404_001"))
                .andExpect(jsonPath("$.message").value("산을 찾을 수 없습니다"));
    }
}
