package com.coworking.reservationservice.controller;

import com.coworking.reservationservice.entity.Reservation;
import com.coworking.reservationservice.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "Création et gestion du cycle de vie des réservations")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    @Operation(summary = "Lister toutes les réservations")
    public List<Reservation> findAll() {
        return reservationService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir une réservation par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réservation trouvée"),
            @ApiResponse(responseCode = "500", description = "Réservation introuvable")
    })
    public Reservation findById(@Parameter(description = "Identifiant de la réservation") @PathVariable Long id) {
        return reservationService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Créer une réservation",
            description = "Vérifie que la salle est disponible (Room Service) et que le membre n'est pas suspendu (Member Service). " +
                    "Si le quota du membre est atteint après création, un événement Kafka est publié pour le suspendre."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Réservation créée avec le statut CONFIRMED"),
            @ApiResponse(responseCode = "500", description = "Salle indisponible ou membre suspendu")
    })
    public Reservation create(@RequestBody Reservation reservation) {
        return reservationService.create(reservation);
    }

    @PutMapping("/{id}/cancel")
    @Operation(
            summary = "Annuler une réservation",
            description = "Transition CONFIRMED → CANCELLED (State Pattern). " +
                    "Libère la salle et décrémente le compteur du membre. " +
                    "Publie un événement Kafka si le membre était suspendu et repasse sous son quota."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réservation annulée"),
            @ApiResponse(responseCode = "500", description = "Transition invalide (déjà annulée ou complétée)")
    })
    public Reservation cancel(@Parameter(description = "Identifiant de la réservation") @PathVariable Long id) {
        return reservationService.cancel(id);
    }

    @PutMapping("/{id}/complete")
    @Operation(
            summary = "Marquer une réservation comme complétée",
            description = "Transition CONFIRMED → COMPLETED (State Pattern). " +
                    "Libère la salle et décrémente le compteur du membre. " +
                    "Publie un événement Kafka si le membre était suspendu et repasse sous son quota."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Réservation complétée"),
            @ApiResponse(responseCode = "500", description = "Transition invalide (déjà annulée ou complétée)")
    })
    public Reservation complete(@Parameter(description = "Identifiant de la réservation") @PathVariable Long id) {
        return reservationService.complete(id);
    }
}
