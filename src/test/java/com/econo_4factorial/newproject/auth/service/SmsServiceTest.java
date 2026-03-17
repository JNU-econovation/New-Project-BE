package com.econo_4factorial.newproject.auth.service;

import com.econo_4factorial.newproject.auth.exception.InternalServerException.IncorrectVerificationCodeException;
import com.econo_4factorial.newproject.auth.exception.InternalServerException.InvalidVerificationCodeException;
import com.econo_4factorial.newproject.auth.repository.SmsRepository;
import com.econo_4factorial.newproject.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private SmsUtilService smsUtilService;

    @Mock
    private SmsRepository smsRepository;

    private SmsService smsService;

    @BeforeEach
    void setUp() {
        smsService = new SmsService(userService, smsUtilService, smsRepository);
    }

    @Test
    void 인증번호를_생성해서_저장하고_문자를_발송한다() {
        given(smsUtilService.normalizePhoneNumber("010-1234-5678")).willReturn("01012345678");
        given(smsUtilService.generateVerificationCode()).willReturn("654321");

        smsService.sendSms("010-1234-5678");

        verify(userService).validateExistPhoneNumber("010-1234-5678");
        verify(smsRepository).createSmsVerification("01012345678", "654321");
        verify(smsUtilService).sendMessage("01012345678", "654321");
    }

    @Test
    void 인증번호가_일치하면_삭제하고_성공한다() {
        given(smsUtilService.normalizePhoneNumber("010-1234-5678")).willReturn("01012345678");
        given(smsRepository.getSmsVerification("01012345678")).willReturn("123456");

        assertThatCode(() -> smsService.verifySms("010-1234-5678", "123456"))
                .doesNotThrowAnyException();

        verify(smsRepository).deleteSmsVerification("01012345678");
    }

    @Test
    void 저장된_인증번호가_없으면_예외가_발생한다() {
        given(smsUtilService.normalizePhoneNumber("010-1234-5678")).willReturn("01012345678");
        given(smsRepository.getSmsVerification("01012345678")).willReturn(null);

        assertThatThrownBy(() -> smsService.verifySms("010-1234-5678", "123456"))
                .isInstanceOf(InvalidVerificationCodeException.class);
    }

    @Test
    void 인증번호가_다르면_예외가_발생한다() {
        given(smsUtilService.normalizePhoneNumber("010-1234-5678")).willReturn("01012345678");
        given(smsRepository.getSmsVerification("01012345678")).willReturn("654321");

        assertThatThrownBy(() -> smsService.verifySms("010-1234-5678", "123456"))
                .isInstanceOf(IncorrectVerificationCodeException.class);
    }
}
