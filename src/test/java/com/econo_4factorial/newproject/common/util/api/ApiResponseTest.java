package com.econo_4factorial.newproject.common.util.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

class ApiResponseTest {

    @Test
    void 데이터없는_성공응답을_생성한다() {
        ApiResult<ApiResult.SuccessBody<Void>> response = ApiResponse.success(HttpStatus.CREATED);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getStatus()).isEqualTo("success");
        assertThat(response.getBody().getData()).isNull();
    }

    @Test
    void 데이터있는_성공응답을_생성한다() {
        ApiResult<ApiResult.SuccessBody<String>> response = ApiResponse.success("data", HttpStatus.OK);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getStatus()).isEqualTo("success");
        assertThat(response.getBody().getData()).isEqualTo("data");
    }

    @Test
    void 헤더포함_성공응답을_생성한다() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/profile");

        ApiResult<ApiResult.SuccessBody<Void>> response = ApiResponse.success(headers, HttpStatus.CREATED);

        assertThat(response.getHeaders().getFirst("Location")).isEqualTo("/profile");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getStatus()).isEqualTo("success");
    }

    @Test
    void 실패응답을_생성한다() {
        ApiResult<ApiResult.ErrorBody> response = ApiResponse.fail("COMMON400_001", "잘못된 요청", HttpStatus.BAD_REQUEST);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getStatus()).isEqualTo("error");
        assertThat(response.getBody().getErrorCode()).isEqualTo("COMMON400_001");
        assertThat(response.getBody().getMessage()).isEqualTo("잘못된 요청");
    }

    @Test
    void 헤더포함_실패응답을_생성한다() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Retry-After", "60");

        ApiResult<ApiResult.ErrorBody> response = ApiResponse.fail("COMMON500_001", "서버 오류", headers,
                HttpStatus.INTERNAL_SERVER_ERROR);

        assertThat(response.getHeaders().getFirst("Retry-After")).isEqualTo("60");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody().getStatus()).isEqualTo("error");
    }
}
