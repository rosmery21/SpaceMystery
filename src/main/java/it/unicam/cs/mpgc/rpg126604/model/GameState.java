
package it.unicam.cs.mpgc.rpg126604.model;

public class GameState {

    private final Astronaut astronaut;
    private String currentLocationId;

    public GameState(Astronaut astronaut, String currentLocationId) {
        if (astronaut == null) {
            throw new IllegalArgumentException("Astronauta non valido");
        }
        if (currentLocationId == null || currentLocationId.isBlank()) {
            throw new IllegalArgumentException("Posizione non valida");
        }

        this.astronaut = astronaut;
        this.currentLocationId = currentLocationId;
    }

    public Astronaut getAstronaut() {
        return astronaut;
    }

    public String getCurrentLocationId() {
        return currentLocationId;
    }

    public void setCurrentLocationId(String locationId) {
        if (locationId == null || locationId.isBlank()) {
            throw new IllegalArgumentException("Posizione non valida");
        }

        this.currentLocationId = locationId;
    }
}
