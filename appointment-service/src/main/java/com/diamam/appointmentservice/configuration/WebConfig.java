package com.diamam.appointmentservice.configuration;

import com.diamam.appointmentservice.interceptor.LoggingRequestInterceptor;
import com.diamam.appointmentservice.interceptor.TraceIdRequestInterceptor;
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
