package com.econo_4factorial.newproject.common.annotation.validator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ValidEmailPatternValidatorTest {

    private final ValidEmailPatternValidator validator = new ValidEmailPatternValidator();

    @Test
    void 올바른_이메일이면_true를_반환한다() {
        boolean isValid = validator.isValid("test@example.com", null);

        assertThat(isValid).isTrue();
    }

    @Test
    void 잘못된_이메일이면_false를_반환한다() {
        boolean isValid = validator.isValid("invalid-email", null);

        assertThat(isValid).isFalse();
    }
}
