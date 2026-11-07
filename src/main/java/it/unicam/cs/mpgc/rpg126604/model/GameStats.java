
package it.unicam.cs.mpgc.rpg126604.model;

public class GameStats {

    private final Astronaut astronaut;

    public GameStats(Astronaut astronaut) {
        if (astronaut == null) {
            throw new IllegalArgumentException("Astronauta non valido");
        }

        this.astronaut = astronaut;
    }

    public long getDiscoveredCluesCount() {
        return astronaut.getNotebook()
                .stream()
                .filter(Clue::isDiscovered)
                .count();
    }

    public long getVisitedLocationsCount() {
        return astronaut.getVisitedLocations()
                .stream()
                .filter(Location::isVisited)
                .count();
    }

    public long getPublishedReportsCount() {
        return astronaut.getReports()
                .stream()
                .filter(Report::isPublished)
                .count();
    }

    public int getTotalReputation() {
        return astronaut.getReputation();
    }
}
