package com.econo_4factorial.newproject.user.service;

import com.econo_4factorial.newproject.auth.dto.apple.AppleUserInfoDTO;
import com.econo_4factorial.newproject.auth.dto.kakao.KakaoUserInfoDTO;
import com.econo_4factorial.newproject.user.domain.User;
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
    public Boolean isProfileFilled(Long userId) {
        User user = findUserByIdOrThrow(userId);
        return user.isProfileFilled();
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
}
