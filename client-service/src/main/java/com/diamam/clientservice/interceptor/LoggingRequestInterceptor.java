package com.diamam.clientservice.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoggingRequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("request method:{}, url:{}", request.getMethod(), extractUrl(request));
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("response method:{}, url:{}, status: {}", request.getMethod(), extractUrl(request), response.getStatus());
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    private String extractUrl(HttpServletRequest request) {
        return request.getQueryString() == null ?
                request.getRequestURI() : request.getRequestURI() + "?" + request.getQueryString();
    }
}
