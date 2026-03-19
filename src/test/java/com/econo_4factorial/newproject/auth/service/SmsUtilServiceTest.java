package com.econo_4factorial.newproject.auth.service;

import com.econo_4factorial.newproject.auth.exception.InternalServerException.FailToSendSmsException;
import com.econo_4factorial.newproject.common.config.SmsProperties;
import com.solapi.sdk.message.dto.response.MultipleDetailMessageSentResponse;
import com.solapi.sdk.message.model.Message;
import com.solapi.sdk.message.service.DefaultMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SmsUtilServiceTest {

    @Mock
    private DefaultMessageService messageService;

    @Mock
    private MultipleDetailMessageSentResponse response;

    private SmsUtilService smsUtilService;

    @BeforeEach
    void setUp() {
        SmsProperties smsProperties = new SmsProperties();
        smsProperties.setKey("key");
        smsProperties.setSecret("secret");
        smsProperties.setSenderNumber("01011112222");
        smsProperties.setSmsPrefix("[산결]");
        smsProperties.setTtl(180);
        smsUtilService = new SmsUtilService(smsProperties);
        smsUtilService.messageService = messageService;
    }

    @Test
    void 인증번호_문자를_발송한다() throws Exception {
        when(messageService.send(any(Message.class))).thenReturn(response);

        MultipleDetailMessageSentResponse result = smsUtilService.sendMessage("01012345678", "123456");

        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
        verify(messageService).send(captor.capture());
        Message message = captor.getValue();
        assertThat(result).isEqualTo(response);
        assertThat(message.getFrom()).isEqualTo("01011112222");
        assertThat(message.getTo()).isEqualTo("01012345678");
        assertThat(message.getText()).isEqualTo("[산결] 본인확인 인증번호는 123456 입니다.");
    }

    @Test
    void 문자발송에_실패하면_예외가_발생한다() throws Exception {
        when(messageService.send(any(Message.class))).thenThrow(new RuntimeException("fail"));

        assertThatThrownBy(() -> smsUtilService.sendMessage("01012345678", "123456"))
                .isInstanceOf(FailToSendSmsException.class);
    }

    @Test
    void 인증번호는_6자리_숫자로_생성한다() {
        String verificationCode = smsUtilService.generateVerificationCode();

        assertThat(verificationCode).matches("\\d{6}");
    }

    @Test
    void 전화번호에서_숫자만_남긴다() {
        String normalizedPhoneNumber = smsUtilService.normalizePhoneNumber("010-1234 5678");

        assertThat(normalizedPhoneNumber).isEqualTo("01012345678");
    }
}
