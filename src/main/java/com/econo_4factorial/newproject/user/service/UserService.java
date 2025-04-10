package com.econo_4factorial.newproject.user.service;

import com.econo_4factorial.newproject.user.domain.User;
import com.econo_4factorial.newproject.user.dto.UserInfoDTO;
import com.econo_4factorial.newproject.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.econo_4factorial.newproject.user.mapper.UserMapper.toEntity;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public User findOrCreateUserByUserInfo(UserInfoDTO userInfoDTO) {
        return userRepository.findByUserInfo_NameAndUserInfo_PhoneNumber(userInfoDTO.name(), userInfoDTO.phoneNumber())
                .orElseGet(()-> addUser(userInfoDTO));
    }

    private User addUser(UserInfoDTO userInfoDTO) {
        return userRepository.save(toEntity(userInfoDTO));
    }


}
