
package it.unicam.cs.mpgc.rpg126604.controller;

import it.unicam.cs.mpgc.rpg126604.interfaces.GameRepository;
import it.unicam.cs.mpgc.rpg126604.model.*;

import java.util.*;

public class GameController {

    private final GameRepository repository;
    private Astronaut astronaut;
    private final Map<String, Location> locations;
    private final Map<String, NPC> npcs;
    private Location currentLocation;

    public GameController(GameRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository non valido");
        }

        this.repository = repository;
        this.locations = new HashMap<>();
        this.npcs = new HashMap<>();

        initializeGame();
    }

    private void initializeGame() {
        astronaut = new Astronaut("Alex");

        Location salaComandi = new Location(
                "comandi",
                "Sala Comandi",
                "Il centro di controllo dell'astronave. Alcuni sistemi non rispondono."
        );

        Location laboratorio = new Location(
                "laboratorio",
                "Laboratorio Scientifico",
                "Strumenti sparsi ovunque. Qualcuno ha cancellato i risultati delle analisi."
        );

        Location salaMotori = new Location(
                "motori",
                "Sala Motori",
                "Le luci lampeggiano. Si sentono strani rumori provenire dai macchinari."
        );

        Clue clue1 = new Clue(
                "c1",
                "Un registro di bordo con alcune pagine mancanti",
                "comandi"
        );

        Clue clue2 = new Clue(
                "c2",
                "Una provetta contenente una sostanza sconosciuta",
                "laboratorio"
        );

        Clue clue3 = new Clue(
                "c3",
                "Tracce di manomissione nel sistema di ventilazione",
                "motori"
        );

        salaComandi.addClue(clue1);
        laboratorio.addClue(clue2);
        salaMotori.addClue(clue3);

        NPC medico = new NPC("n1", "Dott.ssa Vega", "Medico di bordo");
        medico.addDialogue(
                "Ho trovato qualcosa di strano nelle ultime analisi..."
        );
        medico.addClue(clue2);

        locations.put(salaComandi.getId(), salaComandi);
        locations.put(laboratorio.getId(), laboratorio);
        locations.put(salaMotori.getId(), salaMotori);

        npcs.put(medico.getId(), medico);

        currentLocation = salaComandi;
        astronaut.visitLocation(salaComandi);
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
