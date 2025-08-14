package com.diamam.appointmentservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestConfig {

    @Value("${doctor-service.url:http://localhost:8082}")
    private String doctorServiceUrl;


    @Bean
    public RestClient doctorServiceRestClient(){
        return RestClient.create(doctorServiceUrl);
    }
}
