
package it.unicam.cs.mpgc.rpg126604.model;

import it.unicam.cs.mpgc.rpg126604.interfaces.ReputationCalculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Rappresenta l'astronauta controllato dal giocatore
 * durante la missione SpaceMystery.
 *
 * Estende {@link GameCharacter} e gestisce il taccuino
 * degli indizi, i rapporti investigativi e i luoghi visitati.
 *
 * La reputazione aumenta ogni volta che viene pubblicato
 * un rapporto investigativo.
 *
 * @version 1.0
 */
public class Astronaut extends GameCharacter {

    private int reputation;
    private final List<Clue> notebook;
    private final List<Report> reports;
    private final List<Location> visitedLocations;

    /**
     * Costruttore vuoto necessario per la deserializzazione JSON.
     */
    public Astronaut() {
        super();
        this.notebook = new ArrayList<>();
        this.reports = new ArrayList<>();
        this.visitedLocations = new ArrayList<>();
    }

    /**
     * Costruisce un astronauta con il nome specificato.
     *
     * @param name il nome dell'astronauta
     * @throws IllegalArgumentException se il nome è nullo o vuoto
     */
    public Astronaut(String name) {
        super(name, "Astronauta");

        this.reputation = 0;
        this.notebook = new ArrayList<>();
        this.reports = new ArrayList<>();
        this.visitedLocations = new ArrayList<>();
    }

    /**
     * Presenta l'astronauta e il suo ruolo nella missione.
     */
    @Override
    public void presentati() {
        System.out.println("Sono " + getName()
                + ", astronauta a bordo della SpaceMystery.");
    }

    /**
     * Restituisce la reputazione attuale dell'astronauta.
     *
     * @return il valore della reputazione
     */
    public int getReputation() {
        return reputation;
    }

    /**
     * Aggiunge un indizio al taccuino dell'astronauta.
     * Se l'indizio non è già presente, viene aggiunto
     * e marcato come scoperto.
     *
     * @param clue l'indizio da aggiungere
     * @throws IllegalArgumentException se l'indizio è nullo
     */
    public void addClueToNotebook(Clue clue) {
        if (clue == null) {
            throw new IllegalArgumentException("Indizio non valido");
        }

        if (!notebook.contains(clue)) {
            notebook.add(clue);
            clue.discover();
        }
    }

    /**
     * Aggiunge un rapporto alla lista dei rapporti
     * investigativi dell'astronauta.
     *
     * @param report il rapporto da aggiungere
     * @throws IllegalArgumentException se il rapporto è nullo
     */
    public void addReport(Report report) {
        if (report == null) {
            throw new IllegalArgumentException("Rapporto non valido");
        }

        if (!reports.contains(report)) {
            reports.add(report);
        }
    }

    /**
     * Pubblica un rapporto investigativo e aggiorna
     * la reputazione dell'astronauta.
     *
     * Il calcolo della reputazione utilizza
     * l'interfaccia funzionale {@link ReputationCalculator}
     * attraverso un'espressione lambda.
     *
     * @param report il rapporto da pubblicare
     * @throws IllegalArgumentException se il rapporto è nullo
     * @throws IllegalStateException se il rapporto è già stato pubblicato
     */
    public void publishReport(Report report) {
        if (report == null) {
            throw new IllegalArgumentException("Rapporto non valido");
        }

        // La regola di calcolo è definita con una lambda.
        ReputationCalculator calculator =
                r -> r.getCluesUsed().size() * 10;

        // Evitiamo di assegnare punti più volte
        // per lo stesso rapporto.
        if (report.isPublished()) {
            throw new IllegalStateException(
                    "Questo rapporto è già stato pubblicato"
            );
        }

        report.publish();
        reputation += calculator.calculate(report);
    }

    /**
     * Registra la visita dell'astronauta a un luogo.
     * Se il luogo non è già stato visitato, viene aggiunto
     * alla lista e marcato come visitato.
     *
     * @param location il luogo da visitare
     * @throws IllegalArgumentException se il luogo è nullo
     */
    public void visitLocation(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("Luogo non valido");
        }

        if (!visitedLocations.contains(location)) {
            visitedLocations.add(location);
            location.visit();
        }
    }

    /**
     * Restituisce una vista non modificabile del taccuino.
     *
     * @return lista non modificabile degli indizi raccolti
     */
    public List<Clue> getNotebook() {
        return Collections.unmodifiableList(notebook);
    }

    /**
     * Restituisce una vista non modificabile dei rapporti.
     *
     * @return lista non modificabile dei rapporti investigativi
     */
    public List<Report> getReports() {
        return Collections.unmodifiableList(reports);
    }

    /**
     * Restituisce una vista non modificabile dei luoghi visitati.
     *
     * @return lista non modificabile dei luoghi visitati
     */
    public List<Location> getVisitedLocations() {
        return Collections.unmodifiableList(visitedLocations);
    }
}
