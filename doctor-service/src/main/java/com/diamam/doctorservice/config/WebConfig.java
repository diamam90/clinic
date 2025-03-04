package com.diamam.doctorservice.config;

import com.diamam.doctorservice.interceptor.LoggingRequestInterceptor;
import com.diamam.doctorservice.interceptor.TraceIdRequestInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TraceIdRequestInterceptor());
        registry.addInterceptor(new LoggingRequestInterceptor());
    }
}
