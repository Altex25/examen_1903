# Design Pattern — State Pattern (Behavioral)

## Microservice concerné

`reservation-service`

## Pattern choisi : State (Comportemental)

### Problème identifié

Le cycle de vie d'une réservation passe par trois états distincts :

```
CONFIRMED ──→ CANCELLED
CONFIRMED ──→ COMPLETED
CANCELLED ──→ transition interdite
COMPLETED ──→ transition interdite
```

Sans pattern, le `ReservationService` contenait des vérifications explicites
sur `ReservationStatus` dans chaque méthode :

```java
if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
    throw new IllegalStateException("Only CONFIRMED reservations can be cancelled");
}
```

Ce code pose deux problèmes :
- **Duplication** : la même logique de vérification est répétée dans `cancel()` et `complete()`
- **Fragilité** : ajouter un nouvel état (ex: `PENDING`, `EXPIRED`) nécessite de modifier
  toutes les méthodes existantes, violant le principe Open/Closed

---

## Solution : State Pattern

Chaque statut est représenté par une classe qui encapsule les transitions qu'il autorise.
Le `ReservationService` délègue la validation à l'état courant sans connaître sa nature.

### Structure

```
ReservationState         (interface)
├── ConfirmedState       → cancel() ✅  complete() ✅
├── CancelledState       → cancel() ❌  complete() ❌
└── CompletedState       → cancel() ❌  complete() ❌

ReservationStateFactory  → instancie l'état selon ReservationStatus
```

### Après refactoring

```java
ReservationState state = ReservationStateFactory.getState(reservation.getStatus());
state.cancel(reservation);
```

Le service ne contient plus aucune vérification explicite sur `ReservationStatus`.

---

## Justification du choix Behavioral vs Creational / Structural

| Catégorie | Pattern envisagé | Pertinence |
|-----------|-----------------|------------|
| **Behavioral** | **State** | Correspond exactement au cycle de vie CONFIRMED → CANCELLED/COMPLETED |
| Creational | Builder | Utile si la construction d'une `Reservation` était complexe (elle ne l'est pas ici) |
| Structural | Facade | `ReservationService` est déjà un Facade implicite sur Room/Member/Kafka, mais ce n'est pas le problème principal à résoudre |

Le State Pattern est le plus adapté car la problématique centrale du `reservation-service`
est la **gestion des transitions d'état avec des règles métier différentes par état**,
ce qui est la définition exacte du pattern.

---

## Extensibilité

L'ajout d'un nouvel état (ex: `PENDING` en attente de confirmation) ne nécessite que :
1. Ajouter `PENDING` à l'enum `ReservationStatus`
2. Créer une classe `PendingState implements ReservationState`
3. Ajouter un cas dans `ReservationStateFactory`

Aucune modification des classes existantes (`ConfirmedState`, `CancelledState`, etc.).
