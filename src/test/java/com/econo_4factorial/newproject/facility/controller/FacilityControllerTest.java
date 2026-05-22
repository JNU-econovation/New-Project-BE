package com.econo_4factorial.newproject.facility.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.econo_4factorial.newproject.common.exception.GlobalExceptionHandler;
import com.econo_4factorial.newproject.common.util.RedirectUriBuilder;
import com.econo_4factorial.newproject.facility.domain.FacilityType;
import com.econo_4factorial.newproject.facility.dto.FacilityDTO;
import com.econo_4factorial.newproject.facility.service.FacilityService;
import com.econo_4factorial.newproject.mountain.exception.BadRequestException.MountainNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class FacilityControllerTest {

    @Mock
    private FacilityService facilityService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        FacilityController controller = new FacilityController(facilityService);
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler(new RedirectUriBuilder());

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(globalExceptionHandler)
                .build();
    }

    @Test
    void 산의_시설목록을_조회한다() throws Exception {
        List<FacilityDTO> facilities = List.of(
                new FacilityDTO(
                        3L,
                        "중머리재 화장실",
                        FacilityType.TOILET,
                        List.of(new BigDecimal("126.9931"), new BigDecimal("35.1402"))
                )
        );
        given(facilityService.getFacilitiesByMountainId(10L)).willReturn(facilities);

        mockMvc.perform(get("/api/v1/facilities")
                        .param("mountainId", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.mountainId").value(10))
                .andExpect(jsonPath("$.data.facilities.length()").value(1))
                .andExpect(jsonPath("$.data.facilities[0].facilityId").value(3))
                .andExpect(jsonPath("$.data.facilities[0].facilityName").value("중머리재 화장실"))
                .andExpect(jsonPath("$.data.facilities[0].facilityType").value("TOILET"));

        verify(facilityService).getFacilitiesByMountainId(10L);
    }

    @Test
    void 없는_산의_시설을_조회하면_예외응답을_반환한다() throws Exception {
        given(facilityService.getFacilitiesByMountainId(99L)).willThrow(new MountainNotFoundException());

        mockMvc.perform(get("/api/v1/facilities")
                        .param("mountainId", "99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.errorCode").value("MOUNTAIN404_001"))
                .andExpect(jsonPath("$.message").value("산을 찾을 수 없습니다"));
    }
}
