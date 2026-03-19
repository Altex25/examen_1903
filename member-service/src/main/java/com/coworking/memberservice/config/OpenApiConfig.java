package com.coworking.memberservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Member Service API",
                version = "1.0.0",
                description = "Gestion des membres de la plateforme : inscription, abonnements et suivi du quota de réservations actives.",
                contact = @Contact(name = "Coworking Platform")
        )
)
public class OpenApiConfig {}
