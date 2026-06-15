package com.econo_4factorial.newproject.facility.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.econo_4factorial.newproject.common.config.QueryDslConfig;
import com.econo_4factorial.newproject.facility.domain.Facility;
import com.econo_4factorial.newproject.facility.domain.FacilityType;
import com.econo_4factorial.newproject.support.MySqlContainerSupport;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(QueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FacilityRepositoryTest extends MySqlContainerSupport {

    @Autowired
    private FacilityRepository facilityRepository;

    @Test
    void 산_아이디로_해당_산의_시설만_조회한다() {
        List<Facility> result = facilityRepository.findByMountainId(1L);

        assertThat(result).isNotEmpty();
        assertThat(result).extracting(Facility::getName).contains("아이더");
        assertThat(result).extracting(facility -> facility.getMountain().getId()).containsOnly(1L);
    }

    @Test
    void 시설이_없는_산은_빈_목록을_반환한다() {
        List<Facility> result = facilityRepository.findByMountainId(2L);

        assertThat(result).isEmpty();
    }

    @Test
    void 시설을_조회하면_타입과_좌표를_담아_반환한다() {
        Facility market = facilityRepository.findByMountainId(1L).stream()
                .filter(facility -> facility.getName().equals("아이더"))
                .findFirst()
                .orElseThrow();

        assertThat(market.getType()).isEqualTo(FacilityType.MARKET);
        assertThat(market.getLatitude()).isEqualByComparingTo(new BigDecimal("35.13323974"));
        assertThat(market.getLongitude()).isEqualByComparingTo(new BigDecimal("126.9570313"));
    }
}
