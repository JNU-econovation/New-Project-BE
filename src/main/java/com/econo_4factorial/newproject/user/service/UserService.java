package com.econo_4factorial.newproject.user.service;

import com.econo_4factorial.newproject.auth.dto.apple.AppleUserInfoDTO;
import com.econo_4factorial.newproject.auth.dto.kakao.KakaoUserInfoDTO;
import com.econo_4factorial.newproject.user.domain.BloodType;
import com.econo_4factorial.newproject.user.domain.User;
import com.econo_4factorial.newproject.user.dto.UserAlertSettingDTO;
import com.econo_4factorial.newproject.user.dto.UserStatusInfoDTO;
import com.econo_4factorial.newproject.user.dto.req.AddPersonalInformationReq;
import com.econo_4factorial.newproject.user.dto.req.AlertSettingReq;
import com.econo_4factorial.newproject.user.dto.req.AddBasicInformationReq;
import com.econo_4factorial.newproject.user.exeception.BadRequestException.EmailAlreadyExistsException;
import com.econo_4factorial.newproject.user.exeception.BadRequestException.PhoneNumberAlreadyExistsException;
import com.econo_4factorial.newproject.user.exeception.BadRequestException.UserNotFoundException;
import com.econo_4factorial.newproject.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.econo_4factorial.newproject.user.mapper.UserMapper.toEntity;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User findUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public User findOrCreateUserByKakaoUserInfo(KakaoUserInfoDTO userInfoDTO) {
        return userRepository.findByKakaoId(userInfoDTO.kakaoId())
                .orElseGet(()-> addKakaoUser(userInfoDTO));
    }

    private User addKakaoUser(KakaoUserInfoDTO userInfoDTO) {
        return userRepository.save(toEntity(userInfoDTO));
    }

    @Transactional
    public User findOrCreateUserByAppleUserInfo(AppleUserInfoDTO userInfo) {
        return userRepository.findByAppleSub(userInfo.appleSub())
                .orElseGet(() -> addAppleUser(userInfo));
    }

    private User addAppleUser(AppleUserInfoDTO userInfo) {
        return userRepository.save(toEntity(userInfo));
    }

    @Transactional(readOnly = true)
    public UserStatusInfoDTO isProfileSet(Long userId) {
        boolean basicInfo = isBasicInfoSet(userId);
        boolean personalInfo = isPersonalInfoSet(userId);
        return new UserStatusInfoDTO(basicInfo, personalInfo);
    }

    private Boolean isBasicInfoSet(Long userId) {
        User user = findUserByIdOrThrow(userId);
        return user.isBasicInfoSet();
    }

    private Boolean isPersonalInfoSet(Long userId) {
        User user = findUserByIdOrThrow(userId);
        return user.isPersonalInfoSet();
    }

    @Transactional(readOnly = true)
    public boolean isNicknameUnique(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

    @Transactional(readOnly = true)
    public void validateExistPhoneNumber(String phoneNumber) {
        if(userRepository.existsByUserInfoPhoneNumber(phoneNumber)) {
            throw new PhoneNumberAlreadyExistsException();
        }
    }

    @Transactional(readOnly = true)
    public void validateExistEmail(String email) {
        if(userRepository.existsByUserInfoEmail(email)) {
            throw new EmailAlreadyExistsException();
        }
    }

    @Transactional
    public void registerBasicInformation(Long userId, AddBasicInformationReq addBasicInformationReq) {
        validateExistEmail(addBasicInformationReq.email());
        User user = findUserByIdOrThrow(userId);
        user.registerBasicInformation(addBasicInformationReq.nickname(), addBasicInformationReq.phoneNumber(),
                addBasicInformationReq.email());
    }

    @Transactional
    public void registerPersonalInformation(Long userId, AddPersonalInformationReq addPersonalInformationReq) {
        User user = findUserByIdOrThrow(userId);
        BloodType bloodType = BloodType.fromString(addPersonalInformationReq.bloodType());
        user.registerPersonalInformation(addPersonalInformationReq.name(), addPersonalInformationReq.weight(),
                addPersonalInformationReq.height(), bloodType);
    }

    @Transactional(readOnly = true)
    public UserAlertSettingDTO getUserAlertSetting(Long userId) {
        User user = findUserByIdOrThrow(userId);
        return UserAlertSettingDTO.from(user.getUserAlert());
    }

    @Transactional
    public void updateAlertSetting(Long userId, AlertSettingReq alertSettingReq) {
        User user = findUserByIdOrThrow(userId);
        user.updateUserAlert(
                Boolean.TRUE.equals(alertSettingReq.eventAlert()),
                Boolean.TRUE.equals(alertSettingReq.travelDeviationAlert()),
                Boolean.TRUE.equals(alertSettingReq.accidentProneAreaAlert())
        );
    }
}
