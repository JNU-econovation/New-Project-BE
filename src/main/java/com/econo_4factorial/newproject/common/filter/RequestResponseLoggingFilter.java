package com.econo_4factorial.newproject.common.filter; // ✅ 님의 프로젝트 구조에 맞게 수정

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component; // ✅ @Component 임포트
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component // ✅ [핵심] 이 어노테이션이 필터를 자동으로 등록시킵니다.
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 요청/응답을 캐싱 가능한 Wrapper로 감쌉니다.
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        try {
            // [중요] 다음 필터 체인을 실행 (컨트롤러 실행)
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            // [중요] 컨트롤러 실행 *후*에 로그를 기록합니다.
            
            // ⚠️ 주의: 운영 환경에서는 개인정보(비밀번호 등)가 노출될 수 있으니
            // 개발 환경에서만 사용하거나, 민감 정보를 마스킹해야 합니다.
            logRequest(wrappedRequest);
            logResponse(wrappedResponse);

            // [필수!] 캐싱된 응답 본문을 실제 response에 복사합니다.
            wrappedResponse.copyBodyToResponse();
        }
    }

    // 요청 본문 로깅
    private void logRequest(ContentCachingRequestWrapper request) {
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            String requestBody = new String(content, StandardCharsets.UTF_8);
            log.info("[REQUEST] {} | BODY={}", 
                    request.getRequestURI(), 
                    requestBody.replaceAll("[\r\n\t]", "")); // 줄바꿈 제거
        }
    }

    // 응답 본문 로깅
    private void logResponse(ContentCachingResponseWrapper response) {
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            String responseBody = new String(content, StandardCharsets.UTF_8);
            log.info("[RESPONSE] {} | BODY={}", 
                    response.getStatus(), 
                    responseBody.replaceAll("[\r\n\t]", "")); // 줄바꿈 제거
        }
    }
}
