
package it.unicam.cs.mpgc.rpg126604.model;

import it.unicam.cs.mpgc.rpg126604.interfaces.ReputationCalculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Astronaut extends GameCharacter {

    private int reputation;
    private final List<Clue> notebook;
    private final List<Report> reports;
    private final List<Location> visitedLocations;

    // Costruttore vuoto per la deserializzazione JSON
    public Astronaut() {
        super();
        this.notebook = new ArrayList<>();
        this.reports = new ArrayList<>();
        this.visitedLocations = new ArrayList<>();
    }

    public Astronaut(String name) {
        super(name, "Astronauta");

        this.reputation = 0;
        this.notebook = new ArrayList<>();
        this.reports = new ArrayList<>();
        this.visitedLocations = new ArrayList<>();
    }

    @Override
    public void presentati() {
        System.out.println("Sono " + getName()
                + ", astronauta a bordo della SpaceMystery.");
    }

    public int getReputation() {
        return reputation;
    }

    public void addClueToNotebook(Clue clue) {
        if (clue == null) {
            throw new IllegalArgumentException("Indizio non valido");
        }

        if (!notebook.contains(clue)) {
            notebook.add(clue);
            clue.discover();
        }
    }

    public void addReport(Report report) {
        if (report == null) {
            throw new IllegalArgumentException("Rapporto non valido");
        }

        if (!reports.contains(report)) {
            reports.add(report);
        }
    }

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

    public void visitLocation(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("Luogo non valido");
        }

        if (!visitedLocations.contains(location)) {
            visitedLocations.add(location);
            location.visit();
        }
    }

    public List<Clue> getNotebook() {
        return Collections.unmodifiableList(notebook);
    }

    public List<Report> getReports() {
        return Collections.unmodifiableList(reports);
    }

    public List<Location> getVisitedLocations() {
        return Collections.unmodifiableList(visitedLocations);
    }
}
