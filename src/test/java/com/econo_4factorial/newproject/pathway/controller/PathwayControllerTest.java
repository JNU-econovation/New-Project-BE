package com.econo_4factorial.newproject.pathway.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.econo_4factorial.newproject.common.constant.Difficulty;
import com.econo_4factorial.newproject.common.exception.GlobalExceptionHandler;
import com.econo_4factorial.newproject.common.util.RedirectUriBuilder;
import com.econo_4factorial.newproject.pathway.dto.PathwayDTO;
import com.econo_4factorial.newproject.pathway.service.PathwayService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.PrecisionModel;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ExtendWith(MockitoExtension.class)
class PathwayControllerTest {

    @Mock
    private PathwayService pathwayService;

    private MockMvc mockMvc;
    private GeometryFactory geometryFactory;

    @BeforeEach
    void setUp() {
        PathwayController controller = new PathwayController(pathwayService);
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler(new RedirectUriBuilder());
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(globalExceptionHandler)
                .setValidator(validator)
                .build();
    }

    @Test
    void 코스의_경로목록을_조회한다() throws Exception {
        LineString coordinates = geometryFactory.createLineString(new Coordinate[]{
                new Coordinate(126.0, 37.0),
                new Coordinate(126.1, 37.1)
        });
        List<PathwayDTO> pathways = List.of(
                PathwayDTO.builder()
                        .pathwayId(1L)
                        .deptBaseId(2L)
                        .destBaseId(3L)
                        .difficulty(Difficulty.NORMAL)
                        .coordinates(coordinates)
                        .build()
        );
        given(pathwayService.getPathwaysByCourseId(5L)).willReturn(pathways);

        mockMvc.perform(get("/api/v1/pathways")
                        .param("courseId", "5")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.pathways.length()").value(1))
                .andExpect(jsonPath("$.data.pathways[0].pathwayId").value(1))
                .andExpect(jsonPath("$.data.pathways[0].difficulty").value("NORMAL"))
                .andExpect(jsonPath("$.data.pathways[0].coordinates[0][0]").value(126.0))
                .andExpect(jsonPath("$.data.pathways[0].coordinates[0][1]").value(37.0));

        verify(pathwayService).getPathwaysByCourseId(5L);
    }

    @Test
    void 필수_쿼리스트링이_없으면_예외응답을_반환한다() throws Exception {
        mockMvc.perform(get("/api/v1/pathways")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.errorCode").value("COMMON400_004"))
                .andExpect(jsonPath("$.message").value("쿼리 스트링이 누락됐습니다."));
    }
}
