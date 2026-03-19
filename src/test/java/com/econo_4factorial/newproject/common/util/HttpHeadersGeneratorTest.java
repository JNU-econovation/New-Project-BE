package com.econo_4factorial.newproject.common.util;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

class HttpHeadersGeneratorTest {

    @Test
    void Location_헤더를_설정한다() {
        HttpHeaders headers = HttpHeadersGenerator.setLocation("https://example.com/profile");

        assertThat(headers.getLocation()).isEqualTo(URI.create("https://example.com/profile"));
    }
}
