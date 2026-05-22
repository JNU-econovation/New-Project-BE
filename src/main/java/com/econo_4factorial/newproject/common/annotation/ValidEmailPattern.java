package com.econo_4factorial.newproject.common.annotation;

import com.econo_4factorial.newproject.common.annotation.validator.ValidEmailPatternValidator;
import com.econo_4factorial.newproject.common.exception.ValidationMessage;
import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidEmailPatternValidator.class)
public @interface ValidEmailPattern {
    String message() default ValidationMessage.EMAIL_PATTERN_INVALID;

    Class[] groups() default {};

    Class[] payload() default {};
}
