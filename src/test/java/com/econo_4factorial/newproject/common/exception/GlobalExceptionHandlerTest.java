package com.econo_4factorial.newproject.common.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.econo_4factorial.newproject.auth.exception.BadRequestException.AuthException;
import com.econo_4factorial.newproject.common.util.RedirectUriBuilder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RedirectUriBuilder redirectUriBuilder = new RedirectUriBuilder();
        ReflectionTestUtils.setField(redirectUriBuilder, "baseUri", "https://example.com/oauth/callback");
        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler(redirectUriBuilder);
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(new DummyController())
                .setControllerAdvice(globalExceptionHandler)
                .setValidator(validator)
                .build();
    }

    @Test
    void 유효성_예외를_공통_응답으로_반환한다() throws Exception {
        mockMvc.perform(post("/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.errorCode").value("COMMON400_001"))
                .andExpect(jsonPath("$.message").value("name이 누락되었습니다"));
    }

    @Test
    void 인증_예외는_리다이렉트_헤더와_함께_반환한다() throws Exception {
        mockMvc.perform(get("/auth"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "https://example.com/oauth/callback"))
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.errorCode").value("AUTH400_001"));
    }

    @Test
    void 쿼리스트링_누락_예외를_공통_응답으로_반환한다() throws Exception {
        mockMvc.perform(get("/request-param"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.errorCode").value("COMMON400_004"));
    }

    @Test
    void 내부서버_예외를_공통_응답으로_반환한다() throws Exception {
        mockMvc.perform(get("/internal"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.errorCode").value("COMMON500_002"));
    }

    @Test
    void 예상치못한_예외를_공통_응답으로_반환한다() throws Exception {
        mockMvc.perform(get("/unexpected"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.errorCode").value("COMMON500_001"));
    }

    @RestController
    private static class DummyController {

        @PostMapping("/validation")
        void validation(@RequestBody @Valid DummyRequest request) {
        }

        @GetMapping("/auth")
        void auth() {
            throw new AuthException();
        }

        @GetMapping("/request-param")
        void requestParam(@RequestParam String name) {
        }

        @GetMapping("/internal")
        void internal() {
            throw new InternalServerException(CommonErrorType.REDIS_NOT_READY_EXCEPTION);
        }

        @GetMapping("/unexpected")
        void unexpected() {
            throw new RuntimeException("boom");
        }
    }

    private record DummyRequest(
            @NotBlank(message = "name이 누락되었습니다")
            String name
    ) {
    }
}
