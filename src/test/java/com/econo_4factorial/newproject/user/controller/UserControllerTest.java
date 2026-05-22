package com.econo_4factorial.newproject.user.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.econo_4factorial.newproject.common.annotation.UserId;
import com.econo_4factorial.newproject.common.exception.GlobalExceptionHandler;
import com.econo_4factorial.newproject.common.exception.ValidationMessage;
import com.econo_4factorial.newproject.common.util.RedirectUriBuilder;
import com.econo_4factorial.newproject.user.domain.BloodType;
import com.econo_4factorial.newproject.user.dto.ProfileStatusInfoDTO;
import com.econo_4factorial.newproject.user.dto.UserAlertSettingDTO;
import com.econo_4factorial.newproject.user.dto.UserProfileDTO;
import com.econo_4factorial.newproject.user.service.RandomNicknameService;
import com.econo_4factorial.newproject.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private RandomNicknameService randomNicknameService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RedirectUriBuilder redirectUriBuilder = new RedirectUriBuilder();
        UserController controller = new UserController(userService, randomNicknameService);
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler(redirectUriBuilder);
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(globalExceptionHandler)
                .setCustomArgumentResolvers(new UserIdArgumentResolver())
                .setValidator(validator)
                .build();
    }

    @Test
    void 프로필을_조회한다() throws Exception {
        given(userService.getUserProfile(1L)).willReturn(new UserProfileDTO(
                "홍길동", "등산러", "010-1234-5678", "test@example.com", 70L, 175L, BloodType.AB, "메모"
        ));

        mockMvc.perform(get("/api/v1/users/profile")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.userProfileDTO.nickname").value("등산러"))
                .andExpect(jsonPath("$.data.userProfileDTO.bloodType").value("AB"));
    }

    @Test
    void 프로필을_수정한다() throws Exception {
        mockMvc.perform(post("/api/v1/users/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "홍길동",
                                  "email": "test@example.com",
                                  "nickname": "등산러",
                                  "phoneNumber": "010-1234-5678",
                                  "weight": 70,
                                  "height": 175,
                                  "bloodType": "A",
                                  "etc": "메모"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void 프로필_설정상태를_조회한다() throws Exception {
        given(userService.isProfileSet(1L)).willReturn(new ProfileStatusInfoDTO(true, false));

        mockMvc.perform(get("/api/v1/users/profile/status")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.profileStatusInfoDTO.isBasicInfoSet").value(true))
                .andExpect(jsonPath("$.data.profileStatusInfoDTO.isPersonalInfoSet").value(false));
    }

    @Test
    void 기본정보를_등록한다() throws Exception {
        mockMvc.perform(post("/api/v1/users/profile/basic-information")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nickname": "등산러",
                                  "phoneNumber": "010-1234-5678",
                                  "email": "test@example.com"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    void 개인정보를_등록한다() throws Exception {
        mockMvc.perform(post("/api/v1/users/profile/personal-information")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "홍길동",
                                  "weight": 70,
                                  "height": 175,
                                  "bloodType": "AB"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    void 알림설정을_조회한다() throws Exception {
        given(userService.getUserAlertSetting(1L)).willReturn(new UserAlertSettingDTO(true, false, true));

        mockMvc.perform(get("/api/v1/users/alert")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.userAlertSetting.eventAlert").value(true))
                .andExpect(jsonPath("$.data.userAlertSetting.travelDeviationAlert").value(false));
    }

    @Test
    void 알림설정을_수정한다() throws Exception {
        mockMvc.perform(put("/api/v1/users/alert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "eventAlert": true,
                                  "travelDeviationAlert": false,
                                  "accidentProneAreaAlert": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    void 랜덤_닉네임을_조회한다() throws Exception {
        given(randomNicknameService.getRandomNickname()).willReturn("무등산1234");

        mockMvc.perform(get("/api/v1/users/nickname/random")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.nickname").value("무등산1234"));
    }

    @Test
    void 닉네임_중복여부를_조회한다() throws Exception {
        given(userService.isNicknameUnique("등산러")).willReturn(true);

        mockMvc.perform(get("/api/v1/users/nickname/check")
                        .param("nickname", "등산러")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.isAvailable").value(true));

        verify(userService).isNicknameUnique("등산러");
    }

    @Test
    void 필수_닉네임_쿼리가_없으면_예외응답을_반환한다() throws Exception {
        mockMvc.perform(get("/api/v1/users/nickname/check")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.errorCode").value("COMMON400_001"))
                .andExpect(jsonPath("$.message").value(ValidationMessage.NICKNAME_IS_REQUIRED));
    }

    private static class UserIdArgumentResolver implements HandlerMethodArgumentResolver {
        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return parameter.hasParameterAnnotation(UserId.class);
        }

        @Override
        public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
            return 1L;
        }
    }
}
