# Plateforme de réservation de salles de coworking

Cette application permet de gérer des salles de coworking, des membres et des réservations.  
Suivez les étapes ci-dessous **dans l'ordre** pour démarrer et utiliser l'application.

---

## Ce dont vous avez besoin avant de commencer

Installez les outils suivants si ce n'est pas déjà fait :


| Outil             | Vérification      | Téléchargement                                                                                   |
| ----------------- | ----------------- | ------------------------------------------------------------------------------------------------ |
| **Java 17+**      | `java -version`   | [https://adoptium.net](https://adoptium.net)                                                     |
| **Maven 3.8+**    | `mvn -version`    | [https://maven.apache.org](https://maven.apache.org)                                             |
| **Docker**        | `docker -version` | [https://www.docker.com/products/docker-desktop](https://www.docker.com/products/docker-desktop) |
| **Bruno** (tests) | —                 | [https://www.usebruno.com](https://www.usebruno.com)                                             |


> Si une commande de vérification retourne une version, l'outil est déjà installé.

---

## Étape 1 — Démarrer Kafka

L'application utilise Kafka pour communiquer entre ses composants.  
Kafka doit être démarré **en premier**, avant tout le reste.

Ouvrez un terminal à la racine du projet et lancez :

```bash
docker run -d --name zookeeper -p 2181:2181 \
  -e ALLOW_ANONYMOUS_LOGIN=yes \
  bitnami/zookeeper:latest
```

```bash
docker run -d --name kafka -p 9092:9092 \
  -e KAFKA_CFG_ZOOKEEPER_CONNECT=host.docker.internal:2181 \
  -e ALLOW_PLAINTEXT_LISTENER=yes \
  -e KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
  bitnami/kafka:latest
```

**Vérification :** les deux conteneurs doivent apparaître avec le statut `Up` :

```bash
docker ps
```

```
NAMES        STATUS
kafka        Up X seconds
zookeeper    Up X seconds
```

---

## Étape 2 — Compiler l'application

Cette étape prépare les fichiers exécutables. Elle n'est à faire **qu'une seule fois**.

Exécutez les commandes suivantes depuis la racine du projet dans différents terminaux (1 par microservice) :

```bash
cd config-server && mvn clean package -DskipTests && cd .. && \
cd discovery-server && mvn clean package -DskipTests && cd .. && \
cd api-gateway && mvn clean package -DskipTests && cd .. && \
cd room-service && mvn clean package -DskipTests && cd .. && \
cd member-service && mvn clean package -DskipTests && cd .. && \
cd reservation-service && mvn clean package -DskipTests && cd .. && \
```

---

## Étape 3 — Démarrer les services

L'application est composée de 6 services qui doivent être démarrés **dans un ordre précis**.  
Ouvrez **6 terminaux** et exécutez une commande par terminal.

---

### Terminal 1 — Serveur de configuration

```bash
cd config-server
mvn spring-boot:run
```

Attendez le message :

```
Started ConfigServerApplication in X seconds
```

---

### Terminal 2 — Registre de services

```bash
cd discovery-server
mvn spring-boot:run
```

Attendez le message :

```
Started DiscoveryServerApplication in X seconds
```

Vérifiez en ouvrant votre navigateur sur : **[http://localhost:8761](http://localhost:8761)**  
Vous devez voir une page Eureka.

---

### Terminal 3 — Passerelle API

```bash
cd api-gateway
mvn spring-boot:run
```

Attendez le message :

```
Started ApiGatewayApplication in X seconds
```

---

### Terminal 4 — Service des salles

```bash
cd room-service
mvn spring-boot:run
```

Attendez le message :

```
Started RoomServiceApplication in X seconds
```

---

### Terminal 5 — Service des membres

```bash
cd member-service
mvn spring-boot:run
```

Attendez le message :

```
Started MemberServiceApplication in X seconds
```

---

### Terminal 6 — Service des réservations

```bash
cd reservation-service
mvn spring-boot:run
```

Attendez le message :

```
Started ReservationServiceApplication in X seconds
```

---

## Étape 4 — Vérifier que tout fonctionne

Ouvrez **[http://localhost:8761](http://localhost:8761)** dans votre navigateur.  
Vous devez voir les 4 services suivants listés avec le statut **UP** :

```
API-GATEWAY          UP
ROOM-SERVICE         UP
MEMBER-SERVICE       UP
RESERVATION-SERVICE  UP
```

> Si un service n'apparaît pas, attendez 20-30 secondes et rafraîchissez la page.

---

## Étape 5 — Utiliser l'application

Toutes les requêtes passent par l'adresse : **[http://localhost:8080](http://localhost:8080)**

### Adresses disponibles


| Fonctionnalité | Adresse                                                                          |
| -------------- | -------------------------------------------------------------------------------- |
| Salles         | [http://localhost:8080/api/rooms](http://localhost:8080/api/rooms)               |
| Membres        | [http://localhost:8080/api/members](http://localhost:8080/api/members)           |
| Réservations   | [http://localhost:8080/api/reservations](http://localhost:8080/api/reservations) |


### Documentation interactive (Swagger)

Pour explorer et tester toutes les fonctionnalités depuis votre navigateur :


| Service      | Lien                                                                           |
| ------------ | ------------------------------------------------------------------------------ |
| Salles       | [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html) |
| Membres      | [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html) |
| Réservations | [http://localhost:8083/swagger-ui.html](http://localhost:8083/swagger-ui.html) |


### Tester avec Bruno

Une collection de requêtes prêtes à l'emploi est disponible dans le dossier `bruno/`.

1. Ouvrez Bruno
2. Cliquez sur `Open Collection`
3. Sélectionnez le dossier `bruno/` à la racine du projet
4. En haut à droite, sélectionnez l'environnement **local**
5. Vous pouvez maintenant exécuter les requêtes une par une

---

## Étape 5 bis — Scénarios de test (ordre recommandé)

Important :
- Faites tout après un redémarrage des services (H2 vide) pour que les IDs commencent à `1`.
- Après chaque action Kafka (suppression salle/membre), attendez `1-2 secondes` avant de vérifier.

1. Créer des salles (villes/types variés)
   - `create-room`
   - `create-room-open-space-lyon`
   - `create-room-private-office-bordeaux`
   - `create-room-meeting-paris`
2. Créer un membre BASIC
   - `create-member-basic`
3. Réserver une salle (attendu : `CONFIRMED`)
   - `create-reservation`
   - `check-room-availability` (doit retourner `false`)
4. Tenter une réservation sur une salle déjà occupée (doit échouer)
   - `create-reservation-occupied-room`
5. Atteindre le quota BASIC (2 réservations) + suspension
   - `create-second-reservation`
   - `check-member-suspended` (doit retourner `true`)
6. Annuler une réservation pour désuspendre
   - `cancel-reservation-2`
   - `check-member-suspended` (doit retourner `false`)
7. Supprimer une salle et vérifier la propagation Kafka
   - `delete-room` (supprime la salle 1)
   - attendez `1-2 secondes`
   - `get-all-reservations` (les réservations liées à cette salle doivent être en `CANCELLED`)
8. Supprimer un membre et vérifier la propagation Kafka
   - `delete-member` (supprime le membre 1)
   - attendez `1-2 secondes`
   - `get-all-reservations` (doit retourner `[]`)

---

## Postman — collection de tests

Une collection Postman est disponible ici :
`postman/coworking-tests.postman_collection.json`

Pour l’utiliser :
1. Ouvrez Postman
2. `Import` → sélectionnez le fichier JSON
3. Exécutez les requêtes via la gateway `http://localhost:8080`

La collection contient des scénarios complets (salles, membres, réservations, quota/suspended, Kafka).

---

## Étape 6 — Arrêter l'application

Pour arrêter proprement :

1. Dans chaque terminal de service, appuyez sur **Ctrl + C**
2. Arrêtez Kafka et Zookeeper :

```bash
docker stop kafka zookeeper
```

Pour les redémarrer ultérieurement sans les recréer :

```bash
docker start zookeeper kafka
```

---

## Résolution des problèmes courants


| Problème                                                         | Cause probable                                        | Solution                                                                               |
| ---------------------------------------------------------------- | ----------------------------------------------------- | -------------------------------------------------------------------------------------- |
| Un service ne démarre pas                                        | Le Config Server n'est pas encore prêt                | Attendez que le Terminal 1 affiche `Started`, puis réessayez                           |
| Service absent de [http://localhost:8761](http://localhost:8761) | Délai d'enregistrement Eureka                         | Attendez 30 secondes et rafraîchissez                                                  |
| Erreur à la création d'une réservation                           | Room Service ou Member Service pas encore enregistrés | Vérifiez que les 4 services sont UP sur [http://localhost:8761](http://localhost:8761) |
| Le statut `suspended` ne se met pas à jour                       | Kafka n'est pas démarré                               | Vérifiez avec `docker ps` que kafka est `Up`                                           |
| `BUILD FAILURE` à la compilation                                 | Dépendances Maven manquantes                          | Vérifiez votre connexion internet et réessayez                                         |
| Port déjà utilisé                                                | Un autre service tourne sur ce port                   | Arrêtez l'autre service ou redémarrez votre machine                                    |


