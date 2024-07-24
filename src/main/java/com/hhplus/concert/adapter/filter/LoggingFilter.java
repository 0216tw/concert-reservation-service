package com.hhplus.concert.adapter.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class LoggingFilter implements Filter {
    private final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        long startTime = System.currentTimeMillis();

        // request
        log.info("[API] {} 경로 요청", httpRequest.getRequestURL().toString());
        log.info("[API] HTTP 메소드: {}", httpRequest.getMethod());
        httpRequest.getParameterMap().forEach((key, value) ->
                log.info("[API] 요청 파라미터: {}={}", key, String.join(",", value)));

        chain.doFilter(request, response);

        long duration = System.currentTimeMillis() - startTime;

        // 응답 정보 로깅
        log.info("[API] 응답 상태 코드: {}", httpResponse.getStatus());
        httpResponse.getHeaderNames().forEach(headerName ->
                log.info("[API] 응답 헤더: {}={}", headerName, httpResponse.getHeader(headerName)));

        log.info("[API] 요청 처리 시간: {} ms", duration);
    }

    @Override
    public void destroy() {
    }
}
