package com.coworking.memberservice.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Schema(description = "Représente un membre de la plateforme de coworking")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identifiant unique", example = "1")
    private Long id;

    @Schema(description = "Nom complet du membre", example = "Alice Dupont")
    private String fullName;

    @Schema(description = "Adresse email", example = "alice.dupont@email.com")
    private String email;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Type d'abonnement : BASIC (2 réservations max), PRO (5), ENTERPRISE (10)", example = "BASIC")
    private SubscriptionType subscriptionType;

    @Schema(description = "Indique si le membre est suspendu (quota de réservations atteint)", example = "false")
    private boolean suspended;

    @Schema(description = "Nombre maximum de réservations actives simultanées (calculé selon l'abonnement)", example = "2")
    private Integer maxConcurrentBookings;

    @Schema(description = "Nombre de réservations actives en cours", example = "0")
    private Integer activeBookings = 0;
}
