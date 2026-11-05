
package it.unicam.cs.mpgc.rpg126604.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Astronaut {

    private final String name;
    private int reputation;
    private final List<Clue> notebook;
    private final List<Report> reports;
    private final List<Location> visitedLocations;

    public Astronaut(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome non valido");
        }

        this.name = name;
        this.reputation = 0;
        this.notebook = new ArrayList<>();
        this.reports = new ArrayList<>();
        this.visitedLocations = new ArrayList<>();
    }

    public String getName() {
        return name;
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

        report.publish();
        reputation += report.getReputationValue();
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
