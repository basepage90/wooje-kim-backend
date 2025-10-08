package com.wire.remit.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@OpenAPIDefinition(info = @Info(title = "Remit API", description = " 송금 API", version = "v1"))
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi adOpenApi() {
        String[] paths = {"/api/**"};

        return GroupedOpenApi.builder()
                .group("Remit API")
                .pathsToMatch(paths)
                .build();
    }
}
