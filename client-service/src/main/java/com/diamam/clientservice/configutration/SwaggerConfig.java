package com.diamam.clientservice.configutration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Configuration
@OpenAPIDefinition
public class SwaggerConfig {

    static {
        Schema<LocalTime> example = new Schema<>();
        example.example(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        SpringDocUtils.getConfig().replaceWithSchema(LocalTime.class, example);
    }

    @Bean
    public OpenAPI openApi(){
        return new OpenAPI()
                .servers(List.of(new Server().url("/")))
                .info(new Info().title("Client Service API").version("0.0.1"));
    }
}
