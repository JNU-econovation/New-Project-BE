package com.econo_4factorial.newproject.auth.service;

import com.econo_4factorial.newproject.auth.exception.InternalServerException.FailToSendSmsException;
import com.econo_4factorial.newproject.common.config.SmsProperties;
import com.solapi.sdk.message.dto.response.MultipleDetailMessageSentResponse;
import com.solapi.sdk.message.model.Message;
import com.solapi.sdk.message.service.DefaultMessageService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import com.solapi.sdk.SolapiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmsUtilService {

    private final SmsProperties smsProperties;
    DefaultMessageService messageService;

    @PostConstruct
    public void init() {
        this.messageService = SolapiClient.INSTANCE.createInstance(smsProperties.getKey(),smsProperties.getSecret());
    }

    public MultipleDetailMessageSentResponse sendMessage(String toPhoneNumber, String verificationCode) {
        try{
            Message message = new Message();
            message.setFrom(smsProperties.getSenderNumber());
            message.setTo(toPhoneNumber);
            message.setText(smsProperties.getSmsPrefix() + " 본인확인 인증번호는 "+verificationCode+" 입니다.");

            MultipleDetailMessageSentResponse response = this.messageService.send(message);
            log.info("SMS-LOG: TO = {}, From = {}, Text = {}",
                    message.getTo(),message.getFrom(),message.getText());
            return response;
        } catch (Exception e) {
            log.error("SMS 발송 실패 - to={}", toPhoneNumber, e);
            throw new FailToSendSmsException();
        }
    }

    public String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder numStr = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            numStr.append(random.nextInt(10));
        }
        return numStr.toString();
    }

    public String normalizePhoneNumber(String phoneNumber) {
        return phoneNumber.replaceAll("[^0-9]", "");
    }
}
