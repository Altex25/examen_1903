package com.coworking.memberservice.controller;

import com.coworking.memberservice.entity.Member;
import com.coworking.memberservice.service.MemberService;
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
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "Members", description = "CRUD et gestion du quota de réservations des membres")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    @Operation(summary = "Lister tous les membres")
    public List<Member> findAll() {
        return memberService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un membre par son identifiant")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Membre trouvé"),
            @ApiResponse(responseCode = "500", description = "Membre introuvable")
    })
    public Member findById(@Parameter(description = "Identifiant du membre") @PathVariable Long id) {
        return memberService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Créer un nouveau membre",
            description = "Le champ maxConcurrentBookings est calculé automatiquement selon l'abonnement : BASIC=2, PRO=5, ENTERPRISE=10."
    )
    @ApiResponse(responseCode = "201", description = "Membre créé")
    public Member create(@RequestBody Member member) {
        return memberService.create(member);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un membre", description = "Met à jour maxConcurrentBookings si l'abonnement change.")
    public Member update(@Parameter(description = "Identifiant du membre") @PathVariable Long id,
                         @RequestBody Member member) {
        return memberService.update(id, member);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Supprimer un membre",
            description = "Supprime le membre et publie un événement Kafka pour supprimer toutes ses réservations."
    )
    @ApiResponse(responseCode = "204", description = "Membre supprimé")
    public void delete(@Parameter(description = "Identifiant du membre") @PathVariable Long id) {
        memberService.delete(id);
    }

    @GetMapping("/{id}/suspended")
    @Operation(summary = "Vérifier si un membre est suspendu")
    @ApiResponse(responseCode = "200", description = "true si suspendu, false sinon")
    public boolean isSuspended(@Parameter(description = "Identifiant du membre") @PathVariable Long id) {
        return memberService.isSuspended(id);
    }

    @PostMapping("/{id}/booking/increment")
    @Operation(
            summary = "Incrémenter le compteur de réservations actives",
            description = "Endpoint interne appelé par le Reservation Service à la création d'une réservation."
    )
    public Member incrementBookings(@Parameter(description = "Identifiant du membre") @PathVariable Long id) {
        return memberService.incrementActiveBookings(id);
    }

    @PostMapping("/{id}/booking/decrement")
    @Operation(
            summary = "Décrémenter le compteur de réservations actives",
            description = "Endpoint interne appelé par le Reservation Service lors d'une annulation ou complétion."
    )
    public Member decrementBookings(@Parameter(description = "Identifiant du membre") @PathVariable Long id) {
        return memberService.decrementActiveBookings(id);
    }
}
