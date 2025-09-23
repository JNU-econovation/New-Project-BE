package com.econo_4factorial.newproject.auth.service;

import com.econo_4factorial.newproject.auth.exception.InternalServerException.IncorrectVerificationCodeException;
import com.econo_4factorial.newproject.auth.exception.InternalServerException.InvalidVerificationCodeException;
import com.econo_4factorial.newproject.auth.repository.SmsRepository;
import com.econo_4factorial.newproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService {

    private final UserService userService;
    private final SmsUtilService smsUtilService;
    private final SmsRepository smsRepository;

    public void sendSms(String toPhoneNumber) {
        userService.validateExistPhoneNumber(toPhoneNumber);
        String normalizedPhoneNumber = smsUtilService.normalizePhoneNumber(toPhoneNumber);
        String verificationCode = smsUtilService.generateVerificationCode();
        smsRepository.createSmsVerification(normalizedPhoneNumber, verificationCode);
        smsUtilService.sendMessage(normalizedPhoneNumber, verificationCode);
    }

    public void verifySms(String toPhoneNumber, String verificationCode) {
        String normalizedPhoneNumber = smsUtilService.normalizePhoneNumber(toPhoneNumber);
        String storedCode = smsRepository.getSmsVerification(normalizedPhoneNumber);
        if (storedCode == null) {
            throw new InvalidVerificationCodeException();
        }
        if (!storedCode.equals(verificationCode)) {
            throw new IncorrectVerificationCodeException();
        }
        smsRepository.deleteSmsVerification(normalizedPhoneNumber);
    }
}
