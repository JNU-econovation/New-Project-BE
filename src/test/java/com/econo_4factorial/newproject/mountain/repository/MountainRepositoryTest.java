package com.econo_4factorial.newproject.mountain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.econo_4factorial.newproject.common.config.QueryDslConfig;
import com.econo_4factorial.newproject.mountain.domain.Mountain;
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
class MountainRepositoryTest extends MySqlContainerSupport {

    @Autowired
    private MountainRepository mountainRepository;

    @Test
    void 초성_접두사로_산을_조회한다() {
        List<Mountain> result = mountainRepository.findByInitialsStartingWith("ㄷ");

        assertThat(result).extracting(Mountain::getName).containsExactly("덕유산");
    }

    @Test
    void 초성_전체로_산을_조회한다() {
        List<Mountain> result = mountainRepository.findByInitialsStartingWith("ㅁㄷㅅ");

        assertThat(result).extracting(Mountain::getName).containsExactly("무등산");
    }

    @Test
    void 매칭되는_초성이_없으면_빈_목록을_반환한다() {
        List<Mountain> result = mountainRepository.findByInitialsStartingWith("ㅋ");

        assertThat(result).isEmpty();
    }

    @Test
    void 이름에_포함된_키워드로_산을_조회한다() {
        List<Mountain> result = mountainRepository.findByNameContaining("백");

        assertThat(result).extracting(Mountain::getName)
                .containsExactlyInAnyOrder("태백산", "소백산");
    }

    @Test
    void 이름_오름차순으로_전체_산을_조회한다() {
        List<String> names = mountainRepository.findAllByOrderByNameAsc()
                .stream()
                .map(Mountain::getName)
                .toList();

        assertThat(names.indexOf("가야산")).isLessThan(names.indexOf("무등산"));
        assertThat(names.indexOf("무등산")).isLessThan(names.indexOf("한라산"));
    }

    @Test
    void 산을_조회하면_초성과_좌표를_담아_반환한다() {
        Mountain mudeungsan = mountainRepository.findByInitialsStartingWith("ㅁㄷㅅ").getFirst();

        assertThat(mudeungsan.getName()).isEqualTo("무등산");
        assertThat(mudeungsan.getInitials()).isEqualTo("ㅁㄷㅅ");
        assertThat(mudeungsan.getLocation()).isEqualTo("광주");
        assertThat(mudeungsan.getLatitude()).isEqualByComparingTo(new BigDecimal("35.13349412111848"));
        assertThat(mudeungsan.getLongitude()).isEqualByComparingTo(new BigDecimal("126.99068462647199"));
    }
}
