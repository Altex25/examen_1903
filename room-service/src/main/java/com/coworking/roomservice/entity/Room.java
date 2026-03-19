package com.coworking.roomservice.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Data
@Schema(description = "Représente une salle de coworking")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identifiant unique", example = "1")
    private Long id;

    @Schema(description = "Nom de la salle", example = "Salle Alpha")
    private String name;

    @Schema(description = "Ville où se trouve la salle", example = "Paris")
    private String city;

    @Schema(description = "Capacité maximale en personnes", example = "10")
    private Integer capacity;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Type de salle", example = "MEETING_ROOM")
    private RoomType type;

    @Schema(description = "Tarif horaire en euros", example = "50.00")
    private BigDecimal hourlyRate;

    @Schema(description = "Disponibilité actuelle de la salle", example = "true")
    private boolean available;
}
