package com.coworking.reservationservice.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Schema(description = "Représente une réservation de salle de coworking")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identifiant unique", example = "1")
    private Long id;

    @Schema(description = "Identifiant de la salle réservée", example = "1")
    private Long roomId;

    @Schema(description = "Identifiant du membre qui réserve", example = "1")
    private Long memberId;

    @Schema(description = "Date et heure de début", example = "2026-03-20T09:00:00")
    private LocalDateTime startDateTime;

    @Schema(description = "Date et heure de fin", example = "2026-03-20T11:00:00")
    private LocalDateTime endDateTime;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Statut de la réservation. Transitions autorisées : CONFIRMED → CANCELLED ou COMPLETED", example = "CONFIRMED")
    private ReservationStatus status;
}
