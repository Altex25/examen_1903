package com.coworking.roomservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Room Service API",
                version = "1.0.0",
                description = "Gestion des salles de coworking : création, mise à jour, suppression et contrôle de disponibilité.",
                contact = @Contact(name = "Coworking Platform")
        )
)
public class OpenApiConfig {}
