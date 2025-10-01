package com.econo_4factorial.newproject.common.exception;

public class ValidationMessage {
    public static final String IDENTITY_TOKEN_REQUIRED = "identityToken이 누락되었습니다";
    public static final String FAMILY_NAME_REQUIRED = "familyName이 누락되었습니다";
    public static final String GIVEN_NAME_REQUIRED = "givenName이 누락되었습니다";
    public static final String EMAIL_PATTERN_INVALID = "email이 올바르지 않은 패턴입니다";
    public static final String EMAIL_IS_REQUIRED = "email은 필수입니다.";
    public static final String REFRESH_TOKEN_REQUIRED = "refreshToken이 누락되었습니다";
    public static final String COURSE_ID_REQUIRED = "courseId가 누락되었습니다";
    public static final String POSITIVE_NUMBER_REQUIRED = "body의 특정 필드가 양수여야 합니다";
    public static final String NICKNAME_IS_REQUIRED = "nickname이 누락되었습니다.";
    public static final String NICKNAME_LENGTH_INVALID = "닉네임은 2자 이상 12자 이하로 입력해야 합니다.";
    public static final String NICKNAME_PATTERN_INVALID = "닉네임은 한글, 영문, 숫자만 입력할 수 있습니다.";
    public static final String PHONE_NUMBER_IS_REQUIRED = "전화번호는 필수입니다.";
    public static final String PHONE_NUMBER_INVALID = "전화번호 형식이 올바르지 않습니다.";
    public static final String VERIFICATION_CODE_IS_REQUIRED = "인증번호는 필수입니다.";
    public static final String VERIFICATION_CODE_INVALID = "인증번호 형식이 올바르지 않습니다.";
    public static final String EVENT_ALERT_IS_REQUIRED = "eventAlert는 필수입니다.";
    public static final String TRAVEL_DEVIATION_ALERT_IS_REQUIRED = "travelDeviationAlert 는 필수입니다.";
    public static final String ACCIDENT_PRONE_AREA_ALERT_IS_REQUIRED = "accidentProneAreaAlert 는 필수입니다.";
    public static final String NAME_IS_REQUIRED = "이름은 필수 입력 값입니다.";
    public static final String NAME_LENGTH_INVALID = "이름은 2자 이상 10자 이하로 입력해야 합니다.";
    public static final String NAME_ONLY_KOREAN = "이름은 한글만 입력 가능합니다.";
    public static final String WEIGHT_IS_REQUIRED = "몸무게는 필수 입력 값입니다.";
    public static final String WEIGHT_POSITIVE = "몸무게는 양수여야 합니다.";
    public static final String WEIGHT_MAX_LENGTH = "몸무게는 3자리까지만 허용됩니다.";
    public static final String HEIGHT_IS_REQUIRED = "키는 필수 입력 값입니다.";
    public static final String HEIGHT_POSITIVE = "키는 양수여야 합니다.";
    public static final String HEIGHT_MAX_LENGTH = "키는 3자리까지만 허용됩니다.";
    public static final String BLOOD_TYPE_IS_REQUIRED = "혈액형은 필수입니다.";
    public static final String INVALID_BLOOD_TYPE = "혈액형은 A, B, O, AB 중 하나여야 합니다.";
    public static final String ETC_LENGTH_INVALID = "기타사항은 200자 이내로 작성해야 합니다.";
    public static final String IMAGE_URL_REQUIRED = "ImageUrl이 누락되었습니다.";
    public static final String IMAGE_URL_INVALID = "ImageUrl형식이 올바르지 않습니다.";
}
