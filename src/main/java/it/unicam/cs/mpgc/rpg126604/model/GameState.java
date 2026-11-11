package it.unicam.cs.mpgc.rpg126604.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

// dati necessari a ripristinare una partita salvata
public class GameState {

    private final Astronaut astronaut;
    private final String currentLocationId;

    // Stato della missione da conservare tra una sessione e l'altra.
    private final int suspicionLevel;
    private final int daysRemaining;
    private final Set<String> contactedNPCs;

    // Costruttore mantenuto per compatibilità con il codice esistente.
    public GameState(Astronaut astronaut, String currentLocationId) {
        this(astronaut, currentLocationId, 0, 10, Set.of());
    }

    public GameState(Astronaut astronaut,
                     String currentLocationId,
                     int suspicionLevel,
                     int daysRemaining,
                     Set<String> contactedNPCs) {

        if (astronaut == null) {
            throw new IllegalArgumentException("Astronauta non valido");
        }

        if (currentLocationId == null || currentLocationId.isBlank()) {
            throw new IllegalArgumentException("Posizione non valida");
        }

        if (suspicionLevel < 0 || suspicionLevel > 100) {
            throw new IllegalArgumentException("Livello di sospetto non valido");
        }

        if (daysRemaining < 0 || daysRemaining > 10) {
            throw new IllegalArgumentException("Giorni rimanenti non validi");
        }

        if (contactedNPCs == null) {
            throw new IllegalArgumentException("Elenco dei dialoghi non valido");
        }

        this.astronaut = astronaut;
        this.currentLocationId = currentLocationId;
        this.suspicionLevel = suspicionLevel;
        this.daysRemaining = daysRemaining;
        this.contactedNPCs = new HashSet<>(contactedNPCs);
    }

    public Astronaut getAstronaut() {
        return astronaut;
    }

    public String getCurrentLocationId() {
        return currentLocationId;
    }

    public int getSuspicionLevel() {
        return suspicionLevel;
    }

    public int getDaysRemaining() {
        return daysRemaining;
    }

    public Set<String> getContactedNPCs() {
        if (contactedNPCs == null) {
            return Collections.emptySet();
        }

        return Collections.unmodifiableSet(contactedNPCs);
    }
}