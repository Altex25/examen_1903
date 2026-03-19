package com.coworking.reservationservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Reservation Service API",
                version = "1.0.0",
                description = "Gestion des réservations de salles de coworking. " +
                        "Vérifie la disponibilité des salles et le statut des membres avant toute création. " +
                        "Implémente le State Pattern pour les transitions de statut (CONFIRMED → CANCELLED / COMPLETED).",
                contact = @Contact(name = "Coworking Platform")
        )
)
public class OpenApiConfig {}
