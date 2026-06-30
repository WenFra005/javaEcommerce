package com.ecommerce.userservice.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Service API")
                        .version("0.1.0")
                        .description("Microsserviço de gerencimento de usuários com autenticação JWT e suporte a diferentes tipos de usuários (Pessoa Natural e Pessoa Jurídica).")
                        .contact(new Contact()
                                .name("Wendell Francisco")
                                .email("wendellfrancisco2005@hotmail.com")
                                .url("https://github.com/WenFra005/javaEcommerce")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Servidor Local")
                ))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", 
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Token JWT obtido via /auth/login")  
                        ));
                        
    }
}
