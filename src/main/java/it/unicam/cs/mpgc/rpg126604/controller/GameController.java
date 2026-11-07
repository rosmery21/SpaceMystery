
package it.unicam.cs.mpgc.rpg126604.controller;

import it.unicam.cs.mpgc.rpg126604.interfaces.GameRepository;
import it.unicam.cs.mpgc.rpg126604.model.*;
import it.unicam.cs.mpgc.rpg126604.repository.GameDataLoader;

import java.util.*;

public class GameController {

    private final GameRepository repository;
    private final GameDataLoader dataLoader;

    private Astronaut astronaut;
    private final Map<String, Location> locations;
    private final Map<String, NPC> npcs;
    private Location currentLocation;

    public GameController(GameRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository non valido");
        }

        this.repository = repository;
        this.dataLoader = new GameDataLoader();
        this.locations = new LinkedHashMap<>();
        this.npcs = new LinkedHashMap<>();

        initializeGame();
    }

    private void initializeGame() {
        astronaut = new Astronaut("Alex");

        // Caricamento delle stanze dal file locations.json
        for (Location location : dataLoader.loadLocations()) {
            locations.put(location.getId(), location);
        }

        // Caricamento degli indizi dal file clues.json
        for (Clue clue : dataLoader.loadClues()) {
            Location location = locations.get(clue.getLocationId());

            if (location != null) {
                location.addClue(clue);
            }
        }

        // Caricamento degli NPC dal file npcs.json
        for (NPC npc : dataLoader.loadNPCs()) {
            npcs.put(npc.getId(), npc);
        }

        // La partita inizia nella Sala Comandi
        currentLocation = locations.get("sala_comandi");

        if (currentLocation == null) {
            throw new IllegalStateException(
                    "La Sala Comandi non è presente in locations.json"
            );
        }

        astronaut.visitLocation(currentLocation);
    }

    public Astronaut getAstronaut() {
        return astronaut;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public Collection<Location> getAllLocations() {
        return Collections.unmodifiableCollection(locations.values());
    }

    public void moveToLocation(String locationId) {
        Location location = locations.get(locationId);

        if (location == null) {
            throw new IllegalArgumentException("Stanza non trovata");
        }

        currentLocation = location;
        astronaut.visitLocation(location);
    }

    public void collectClue(Clue clue) {
        if (clue == null) {
            throw new IllegalArgumentException("Indizio non valido");
        }

        astronaut.addClueToNotebook(clue);
    }

    public Report createReport(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Titolo non valido");
        }

        Report report = new Report(
                "r" + System.currentTimeMillis(),
                title
        );

        astronaut.addReport(report);
        return report;
    }

    public void publishReport(Report report) {
        astronaut.publishReport(report);
        saveGame();
    }

    public void saveGame() {
        GameState state = new GameState(
                astronaut,
                currentLocation.getId()
        );

        repository.saveGame(state);
    }

    public boolean hasSavedGame() {
        return repository.hasSavedGame();
    }
}
