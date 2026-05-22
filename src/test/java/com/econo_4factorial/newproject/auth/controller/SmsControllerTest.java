package com.econo_4factorial.newproject.auth.controller;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.econo_4factorial.newproject.auth.service.SmsService;
import com.econo_4factorial.newproject.common.annotation.UserId;
import com.econo_4factorial.newproject.common.exception.GlobalExceptionHandler;
import com.econo_4factorial.newproject.common.exception.ValidationMessage;
import com.econo_4factorial.newproject.common.util.RedirectUriBuilder;
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
class SmsControllerTest {

    @Mock
    private SmsService smsService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RedirectUriBuilder redirectUriBuilder = new RedirectUriBuilder();
        SmsController controller = new SmsController(smsService);
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
    void 인증번호를_전송한다() throws Exception {
        mockMvc.perform(post("/api/v1/auth/sms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "phoneNumber": "010-1234-5678"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.phoneNumber").value("010-1234-5678"));

        verify(smsService).sendSms("010-1234-5678");
    }

    @Test
    void 인증번호를_검증한다() throws Exception {
        mockMvc.perform(post("/api/v1/auth/sms/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "phoneNumber": "010-1234-5678",
                                  "verificationCode": "123456"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.phoneNumber").value("010-1234-5678"));

        verify(smsService).verifySms("010-1234-5678", "123456");
    }

    @Test
    void 잘못된_전화번호_형식이면_예외응답을_반환한다() throws Exception {
        mockMvc.perform(post("/api/v1/auth/sms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "phoneNumber": "01012345678"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.errorCode").value("COMMON400_001"))
                .andExpect(jsonPath("$.message").value(ValidationMessage.PHONE_NUMBER_INVALID));
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
