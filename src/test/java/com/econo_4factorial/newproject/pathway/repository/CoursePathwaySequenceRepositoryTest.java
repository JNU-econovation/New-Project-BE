package com.econo_4factorial.newproject.pathway.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.econo_4factorial.newproject.common.config.QueryDslConfig;
import com.econo_4factorial.newproject.pathway.domain.CoursePathwaySequence;
import com.econo_4factorial.newproject.support.MySqlContainerSupport;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(QueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CoursePathwaySequenceRepositoryTest extends MySqlContainerSupport {

    private static final Long COURSE_ID = 1L;

    @Autowired
    private CoursePathwaySequenceRepository coursePathwaySequenceRepository;

    @Test
    void 코스의_경로를_sequence_오름차순으로_조회한다() {
        List<CoursePathwaySequence> result =
                coursePathwaySequenceRepository.findByCourseIdOrderBySequence(COURSE_ID);

        assertThat(result).extracting(CoursePathwaySequence::getSequence)
                .containsExactly(1L, 2L, 3L);
        // sequence 순서대로 증심사주차장(21)→새인봉(11)→서인봉(13)→입석대(28)
        assertThat(result).extracting(cps -> cps.getPathway().getDeparture().getId())
                .containsExactly(21L, 11L, 13L);
        assertThat(result).extracting(cps -> cps.getPathway().getDestination().getId())
                .containsExactly(11L, 13L, 28L);
    }

    @Test
    void 경로가_없는_코스는_빈_목록을_반환한다() {
        List<CoursePathwaySequence> result =
                coursePathwaySequenceRepository.findByCourseIdOrderBySequence(2L);

        assertThat(result).isEmpty();
    }

    @Test
    void 좌표를_읽으면_x는_경도_y는_위도로_매핑된다() {
        CoursePathwaySequence first =
                coursePathwaySequenceRepository.findByCourseIdOrderBySequence(COURSE_ID).getFirst();
        Coordinate firstPoint = first.getPathway().getCoordinates().getCoordinateN(0);

        // 시드 WKT: LINESTRING(35.1334 126.9578 ...) = 위도 경도 순으로 저장했지만,
        // 읽으면 getX=경도(126.9578), getY=위도(35.1334)로 축이 뒤바뀐다.
        assertThat(firstPoint.getX()).isEqualTo(126.9578);
        assertThat(firstPoint.getY()).isEqualTo(35.1334);
    }
}
