package com.coworking.roomservice.controller;

import com.coworking.roomservice.entity.Room;
import com.coworking.roomservice.service.RoomService;
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
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@Tag(name = "Rooms", description = "CRUD et gestion de la disponibilité des salles")
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    @Operation(summary = "Lister toutes les salles")
    public List<Room> findAll() {
        return roomService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir une salle par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Salle trouvée"),
            @ApiResponse(responseCode = "500", description = "Salle introuvable")
    })
    public Room findById(@Parameter(description = "Identifiant de la salle") @PathVariable Long id) {
        return roomService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer une nouvelle salle")
    @ApiResponse(responseCode = "201", description = "Salle créée")
    public Room create(@RequestBody Room room) {
        return roomService.create(room);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une salle existante")
    public Room update(@Parameter(description = "Identifiant de la salle") @PathVariable Long id,
                       @RequestBody Room room) {
        return roomService.update(id, room);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Supprimer une salle",
            description = "Supprime la salle et publie un événement Kafka pour annuler toutes ses réservations CONFIRMED."
    )
    @ApiResponse(responseCode = "204", description = "Salle supprimée")
    public void delete(@Parameter(description = "Identifiant de la salle") @PathVariable Long id) {
        roomService.delete(id);
    }

    @GetMapping("/{id}/available")
    @Operation(summary = "Vérifier la disponibilité d'une salle")
    @ApiResponse(responseCode = "200", description = "true si disponible, false si occupée")
    public boolean isAvailable(@Parameter(description = "Identifiant de la salle") @PathVariable Long id) {
        return roomService.isAvailable(id);
    }

    @PutMapping("/{id}/availability")
    @Operation(
            summary = "Mettre à jour la disponibilité d'une salle",
            description = "Endpoint interne appelé par le Reservation Service lors de la création ou clôture d'une réservation."
    )
    public Room updateAvailability(@Parameter(description = "Identifiant de la salle") @PathVariable Long id,
                                   @Parameter(description = "true = disponible, false = occupée") @RequestParam boolean available) {
        return roomService.updateAvailability(id, available);
    }
}
