package com.econo_4factorial.newproject.common.annotation.validator;

import com.econo_4factorial.newproject.common.annotation.ValidEmailPattern;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ValidEmailPatternValidator implements ConstraintValidator<ValidEmailPattern, String> {
    private static final Pattern VALID_EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,3}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return VALID_EMAIL_PATTERN.matcher(value).matches();
    }
}
