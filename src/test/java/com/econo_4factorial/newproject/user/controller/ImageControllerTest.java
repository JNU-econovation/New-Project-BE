package com.econo_4factorial.newproject.user.controller;

import com.econo_4factorial.newproject.common.annotation.UserId;
import com.econo_4factorial.newproject.common.exception.GlobalExceptionHandler;
import com.econo_4factorial.newproject.common.exception.ValidationMessage;
import com.econo_4factorial.newproject.common.util.RedirectUriBuilder;
import com.econo_4factorial.newproject.user.domain.ImageFileFormat;
import com.econo_4factorial.newproject.user.dto.PresignedUrlDTO;
import com.econo_4factorial.newproject.user.dto.ProfileImageUrlDTO;
import com.econo_4factorial.newproject.user.service.S3Service;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ImageControllerTest {

    @Mock
    private S3Service s3Service;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RedirectUriBuilder redirectUriBuilder = new RedirectUriBuilder();
        ImageController controller = new ImageController(s3Service);
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
    void Presigned_URL을_조회한다() throws Exception {
        given(s3Service.createPresignedUrl(1L, ImageFileFormat.JPG))
                .willReturn(new PresignedUrlDTO("https://s3.com/presigned", "profile/1/test.jpeg"));

        mockMvc.perform(post("/api/v1/users/profile-image")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "imageFileFormat": "JPG"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.presignedUrl").value("https://s3.com/presigned"))
                .andExpect(jsonPath("$.data.fileName").value("profile/1/test.jpeg"));
    }

    @Test
    void 프로필_이미지_URL을_조회한다() throws Exception {
        given(s3Service.getImageUrl(1L)).willReturn(new ProfileImageUrlDTO("https://s3.com/profile.jpeg"));

        mockMvc.perform(get("/api/v1/users/profile-image")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.profileImageUrl").value("https://s3.com/profile.jpeg"));
    }

    @Test
    void 프로필_이미지를_삭제한다() throws Exception {
        mockMvc.perform(delete("/api/v1/users/profile-image")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(s3Service).deleteImageUrl(1L);
    }

    @Test
    void 파일명을_저장한다() throws Exception {
        mockMvc.perform(post("/api/v1/users/profile-image/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "fileName": "profile/1/test.jpeg"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));

        verify(s3Service).saveFileNameToEntity(1L, "profile/1/test.jpeg");
    }

    @Test
    void 파일형식이_없으면_예외응답을_반환한다() throws Exception {
        mockMvc.perform(post("/api/v1/users/profile-image")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.errorCode").value("COMMON400_001"))
                .andExpect(jsonPath("$.message").value(ValidationMessage.FILE_FORMAT_IS_REQUIRED));
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
