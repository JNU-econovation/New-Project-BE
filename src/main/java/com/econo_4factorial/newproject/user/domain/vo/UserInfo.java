package com.econo_4factorial.newproject.user.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@NoArgsConstructor
@Getter
@Embeddable
public class UserInfo {
    private static final Pattern VALID_EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,3}$");
    private static final Pattern VALID_NAME_PATTERN = Pattern.compile(   "^[a-zA-Z가-힣]+$");
    private static final Pattern VALID_PHONE_NUMBER_PATTERN = Pattern.compile(   "^010-?(\\d{4})-?(\\d{4})$");

    private static final String EMAIL = "이메일";
    private static final String NAME = "이름";
    private static final String PHONE_NUMBER = "전화번호";

    private String email;

    private String name;

    private String phoneNumber;

    public UserInfo(String email, String name){
        validateEmail(email);
        validateName(name);
        this.email = email;
        this.name = name;
        this.phoneNumber = null;
    }

    private void validateEmail(String email) {
        requireNotNullAndNotBlank(email, EMAIL);
        requireValidFormat(VALID_EMAIL_PATTERN, email, EMAIL);
    }

    private void validateName(String name) {
        requireNotNullAndNotBlank(name, NAME);
        requireValidFormat(VALID_NAME_PATTERN, name, NAME);
    }

    private void validatePhoneNumber(String phoneNumber) {
        requireNotNullAndNotBlank(phoneNumber, PHONE_NUMBER);
        requireValidFormat(VALID_PHONE_NUMBER_PATTERN, phoneNumber, PHONE_NUMBER);
    }

    private void requireNotNullAndNotBlank(String value, String fieldName ) {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException(fieldName + "값은 null 이거나 공백일 수 없습니다");
    }

    private void requireValidFormat(Pattern pattern, String value, String fieldName ) {
        boolean isValidFormat = pattern.matcher(value).matches();
        if (!isValidFormat)
            throw new IllegalArgumentException(fieldName + "값은 올바르지 않은 형태입니다");
    }

}
