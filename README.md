# TP 19 — Eureka et microservice Client

Implémentation des étapes 2 à 4 fournies. La Gateway (8888) et le service Voiture (8089), annoncés dans la topologie générale, restent à réaliser dans les étapes suivantes.

## Prérequis

- JDK 17 ou 21 recommandé et Maven 3.6.3+ dans le PATH.
- Spring Boot 3.5.7 et Spring Cloud 2025.0.0 : versions compatibles selon la [matrice officielle](https://spring.io/projects/spring-cloud).
- Ports 8761 et 8088 disponibles.

## Compiler et tester

Depuis la racine :

```powershell
mvn clean verify
```

Le test d'intégration du service Client utilise un serveur HTTP réel sur un port aléatoire, sans Eureka. Il vérifie les données initiales, le CRUD, les réponses 404 et le health check.

## Démarrer

Dans un premier terminal :

```powershell
mvn -pl eureka-server spring-boot:run
```

Puis dans un deuxième terminal :

```powershell
mvn -pl service-client spring-boot:run
```

Dans IntelliJ, importer le `pom.xml` racine comme projet Maven, choisir un JDK compatible et lancer `EurekaServerApplication`, puis `ClientApplication`.

## Vérifier

- Registre : <http://localhost:8761/>. Attendre l'apparition de `SERVICE-CLIENT` avec le statut `UP` (l'enregistrement peut prendre quelques secondes).
- Liste : <http://localhost:8088/clients>.
- Premier client : <http://localhost:8088/client/1> (alias `/clients/1`).
- Santé : <http://localhost:8088/actuator/health>.
- Console H2 : <http://localhost:8088/h2-console>, URL JDBC `jdbc:h2:mem:clients`, utilisateur `sa`, mot de passe vide.

La liste contient Rabab SELIMANI (23), Amal RAMI (22) et Samir SAFI (22). La base en mémoire est recréée à chaque démarrage.

| Méthode | Chemin | Résultat |
| --- | --- | --- |
| GET | `/clients` | Liste des clients |
| GET | `/client/{id}` ou `/clients/{id}` | Client, ou 404 |
| POST | `/clients` | Création, statut 201 et en-tête Location |
| PUT | `/clients/{id}` | Modification, ou 404 |
| DELETE | `/clients/{id}` | Suppression, statut 204, ou 404 |

Exemple de création dans PowerShell :

```powershell
Invoke-RestMethod -Method Post -Uri http://localhost:8088/clients -ContentType 'application/json' -Body '{"nom":"Nouveau client","age":25}'
```

Le code suit les couches `controllers`, `services`, `repositories`, `entities`. L'export automatique du repository Data REST est désactivé pour laisser les endpoints au contrôleur. La console H2 et les identifiants vides sont prévus pour ce TP local.
