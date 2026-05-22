package com.econo_4factorial.newproject.user.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.econo_4factorial.newproject.user.domain.BloodType;
import org.junit.jupiter.api.Test;

class PhysicalInfoTest {

    @Test
    void 개인_신체정보를_생성한다() {
        PhysicalInfo physicalInfo = new PhysicalInfo(70L, 180L, BloodType.A, "비고");

        assertThat(physicalInfo.getWeight()).isEqualTo(70L);
        assertThat(physicalInfo.getHeight()).isEqualTo(180L);
        assertThat(physicalInfo.getBloodType()).isEqualTo(BloodType.A);
        assertThat(physicalInfo.getEtc()).isEqualTo("비고");
    }

    @Test
    void 개인_신체정보를_수정한다() {
        PhysicalInfo physicalInfo = new PhysicalInfo(70L, 180L, BloodType.A, "비고");

        physicalInfo.updatePersonalInformation(80L, 175L, BloodType.B, "수정");

        assertThat(physicalInfo.getWeight()).isEqualTo(80L);
        assertThat(physicalInfo.getHeight()).isEqualTo(175L);
        assertThat(physicalInfo.getBloodType()).isEqualTo(BloodType.B);
        assertThat(physicalInfo.getEtc()).isEqualTo("수정");
    }

    @Test
    void 몸무게_범위를_벗어나면_예외가_발생한다() {
        assertThatThrownBy(() -> new PhysicalInfo(0L, 180L, BloodType.A, null))
                .isInstanceOf(java.lang.IllegalArgumentException.class)
                .hasMessage("몸무게는 1~999kg 범위여야 합니다.");
    }

    @Test
    void 키_범위를_벗어나면_예외가_발생한다() {
        assertThatThrownBy(() -> new PhysicalInfo(70L, 1000L, BloodType.A, null))
                .isInstanceOf(java.lang.IllegalArgumentException.class)
                .hasMessage("키는 1~999cm 범위여야 합니다.");
    }

    @Test
    void 비고가_200자를_초과하면_예외가_발생한다() {
        String etc = "a".repeat(201);

        assertThatThrownBy(() -> new PhysicalInfo(70L, 180L, BloodType.A, etc))
                .isInstanceOf(java.lang.IllegalArgumentException.class)
                .hasMessage("비고(etc)는 200자 이하여야 합니다.");
    }

    @Test
    void 키와_몸무게와_혈액형이_있으면_개인정보_등록완료로_판단한다() {
        PhysicalInfo physicalInfo = new PhysicalInfo(70L, 180L, BloodType.AB, null);

        assertThat(physicalInfo.isPersonalInfoSet()).isTrue();
    }
}
