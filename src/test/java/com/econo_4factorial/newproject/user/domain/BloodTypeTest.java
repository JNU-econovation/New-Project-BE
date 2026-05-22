package com.econo_4factorial.newproject.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.econo_4factorial.newproject.user.exception.BadRequestException.InvalidBloodTypeException;
import org.junit.jupiter.api.Test;

class BloodTypeTest {

    @Test
    void 대소문자와_무관하게_혈액형을_변환한다() {
        BloodType bloodType = BloodType.fromString("ab");

        assertThat(bloodType).isEqualTo(BloodType.AB);
    }

    @Test
    void null_혈액형은_일반_예외를_던진다() {
        assertThatThrownBy(() -> BloodType.fromString(null))
                .isInstanceOf(java.lang.IllegalArgumentException.class)
                .hasMessage("bloodType cannot be null");
    }

    @Test
    void 지원하지_않는_혈액형은_커스텀_예외를_던진다() {
        assertThatThrownBy(() -> BloodType.fromString("AC"))
                .isInstanceOf(InvalidBloodTypeException.class);
    }
}
