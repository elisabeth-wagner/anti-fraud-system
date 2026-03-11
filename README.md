# Anti-Fraud System

Ein RESTful API-Dienst zur Erkennung betrügerischer Transaktionen, entwickelt mit Spring Boot.

## Über das Projekt

Dieses System bewertet Finanztransaktionen basierend auf verschiedenen Regeln (IP-Adresse, Kartenregion, Betrag) und dynamischen Limits. Es bietet Funktionen für Händler (Transaktionsprüfung), Support (Feedback und Listenverwaltung) und Administratoren (Benutzerverwaltung).

## Funktionen

*   **Transaktionsprüfung**: Echtzeit-Validierung von Transaktionen. Das System entscheidet basierend auf Regeln und Historie über den Status: `ALLOWED`, `MANUAL_PROCESSING` oder `PROHIBITED`.
*   **Dynamische Limits**: Automatische Anpassung der Grenzwerte basierend auf dem Feedback vom Support-Team.
*   **Schwarze Listen**: Verwaltung von gestohlenen Karten und verdächtigen IP-Adressen.
*   **Sicherheit**: Rollenbasierte Zugriffskontrolle (RBAC):
    *   **ADMINISTRATOR**: Benutzerverwaltung.
    *   **MERCHANT**: Kann Transaktionen zur Prüfung senden.
    *   **SUPPORT**: Verwaltet Listen und gibt Feedback zu Transaktionen.
*   **Korrelationsprüfung**: Erkennung von Mustern (z.B. Nutzung mehrerer Regionen oder IPs innerhalb einer Stunde).

## Technologien

*   Java 17
*   Spring Boot 3
*   Spring Security
*   Spring Data JPA
*   Gradle

## Installation und Start

1.  Repository klonen:
    ```bash
    git clone https://github.com/elisabeth-wagner/anti-fraud-system.git
    ```
2.  In das Verzeichnis wechseln:
    ```bash
    cd anti-fraud-system
    ```
3.  Anwendung starten:
    ```bash
    ./gradlew bootRun
    ```

## Wichtige Endpunkte

### Authentifizierung
*   `POST /api/auth/user` - Benutzer registrieren.
*   `GET /api/auth/list` - Liste aller Benutzer abrufen.

### Anti-Fraud (Händler)
*   `POST /api/antifraud/transaction` - Transaktion bewerten lassen.

### Support & Management
*   `PUT /api/antifraud/feedback` - Feedback zu einer Transaktion geben (ändert Limits).
*   `POST /api/antifraud/suspicious-ip` - IP zur schwarzen Liste hinzufügen.
*   `POST /api/antifraud/stolencard` - Karte zur schwarzen Liste hinzufügen.
