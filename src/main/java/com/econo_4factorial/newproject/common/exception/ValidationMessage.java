package com.econo_4factorial.newproject.common.exception;

public class ValidationMessage {
    public static final String IDENTITY_TOKEN_REQUIRED = "identityToken이 누락되었습니다";
    public static final String FAMILY_NAME_REQUIRED = "familyName이 누락되었습니다";
    public static final String GIVEN_NAME_REQUIRED = "givenName이 누락되었습니다";
    public static final String EMAIL_INVALID_PATTERN = "email이 올바르지 않은 패턴입니다";
    public static final String REFRESH_TOKEN_REQUIRED = "refreshToken이 누락되었습니다";
    public static final String COURSE_ID_REQUIRED = "courseId가 누락되었습니다";
    public static final String POSITIVE_NUMBER_REQUIRED = "body의 특정 필드가 양수여야 합니다";
    public static final String PHONE_NUMBER_IS_REQUIRED = "전화번호는 필수입니다.";
    public static final String PHONE_NUMBER_INVALID = "전화번호 형식이 올바르지 않습니다.";
    public static final String VERIFICATION_CODE_IS_REQUIRED = "인증번호는 필수입니다.";
    public static final String VERIFICATION_CODE_INVALID = "인증번호 형식이 올바르지 않습니다.";
}
