package com.econo_4factorial.newproject.common.util;

import java.net.URI;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;

@UtilityClass
public class HttpHeadersGenerator {

    public static HttpHeaders setLocation(String uri) {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(uri));
        return headers;
    }
}
