package com.batterytrade.app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("BatteryTrade API")
                        .version("1.0")
                        .description(
                                "Sistema de compra y venta de accesorios tecnológicos")
                        .contact(new Contact()
                                .name("BatteryTrade")
                                .email("admin@batterytrade.com")));
    }
}