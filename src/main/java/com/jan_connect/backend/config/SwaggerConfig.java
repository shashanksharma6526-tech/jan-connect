package com.jan_connect.backend.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Full CRUD Backend API",
        version = "1.0",
        description = "Backend for JanConnect app made for Hacknovate 7.0 or sth idk i don't remember",
        contact = @Contact(name = "Shaurya Sharma", email = "shauryasharmathe3rd@gmail.com")
    )
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat =  "JWT"
)
public class SwaggerConfig {
    // Auto scanning done by Spring-Doc
}
