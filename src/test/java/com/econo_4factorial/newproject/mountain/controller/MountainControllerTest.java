package com.econo_4factorial.newproject.mountain.controller;

import com.econo_4factorial.newproject.common.exception.GlobalExceptionHandler;
import com.econo_4factorial.newproject.common.util.RedirectUriBuilder;
import com.econo_4factorial.newproject.mountain.dto.MountainDTO;
import com.econo_4factorial.newproject.mountain.dto.SuggestedMountainDTO;
import com.econo_4factorial.newproject.mountain.service.MountainService;
import com.econo_4factorial.newproject.mountain.service.SuggestMountainService;
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
class MountainControllerTest {

    @Mock
    private MountainService mountainService;

    @Mock
    private SuggestMountainService suggestMountainService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MountainController controller = new MountainController(mountainService, suggestMountainService);
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler(new RedirectUriBuilder());

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(globalExceptionHandler)
                .build();
    }

    @Test
    void 전체_산_목록을_조회한다() throws Exception {
        List<MountainDTO> mountains = List.of(
                new MountainDTO(
                        1L,
                        "무등산",
                        "광주",
                        List.of(new BigDecimal("126.9890"), new BigDecimal("35.1340"))
                )
        );
        given(mountainService.findAll()).willReturn(mountains);

        mockMvc.perform(get("/api/v1/mountains")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.mountains.length()").value(1))
                .andExpect(jsonPath("$.data.mountains[0].id").value(1))
                .andExpect(jsonPath("$.data.mountains[0].name").value("무등산"))
                .andExpect(jsonPath("$.data.mountains[0].location").value("광주"))
                .andExpect(jsonPath("$.data.mountains[0].coordinate[0]").value(126.9890))
                .andExpect(jsonPath("$.data.mountains[0].coordinate[1]").value(35.1340));

        verify(mountainService).findAll();
    }

    @Test
    void 산_자동완성_목록을_조회한다() throws Exception {
        List<SuggestedMountainDTO> suggestions = List.of(
                new SuggestedMountainDTO(
                        2L,
                        "북한산",
                        List.of(new BigDecimal("126.9805"), new BigDecimal("37.6587"))
                )
        );
        given(suggestMountainService.suggestMountains("북한")).willReturn(suggestions);

        mockMvc.perform(get("/api/v1/mountains/searches/suggestions")
                        .param("keyword", "북한")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.suggestedMountainDTOs.length()").value(1))
                .andExpect(jsonPath("$.data.suggestedMountainDTOs[0].id").value(2))
                .andExpect(jsonPath("$.data.suggestedMountainDTOs[0].name").value("북한산"));

        verify(suggestMountainService).suggestMountains("북한");
    }

    @Test
    void 자동완성_키워드가_없으면_예외응답을_반환한다() throws Exception {
        mockMvc.perform(get("/api/v1/mountains/searches/suggestions")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.errorCode").value("COMMON400_004"))
                .andExpect(jsonPath("$.message").value("쿼리 스트링이 누락됐습니다."));
    }
}
