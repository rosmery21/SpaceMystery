
package it.unicam.cs.mpgc.rpg126604.model;

/**
 * Classe astratta che rappresenta un personaggio di SpaceMystery.
 * Ogni personaggio ha un nome e un ruolo nel sistema.
 *
 * Le sottoclassi devono implementare il metodo
 * {@link #presentati()} per definire come il personaggio
 * si presenta.
 *
 * @version 1.0
 */
public abstract class GameCharacter {

    private String name;
    private String role;

    /**
     * Costruttore vuoto necessario per la deserializzazione JSON.
     */
    public GameCharacter() {
    }

    /**
     * Costruisce un personaggio con nome e ruolo specificati.
     *
     * @param name il nome del personaggio
     * @param role il ruolo del personaggio nel gioco
     * @throws IllegalArgumentException se nome o ruolo sono nulli o vuoti
     */
    public GameCharacter(String name, String role) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome non valido");
        }

        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Ruolo non valido");
        }

        this.name = name;
        this.role = role;
    }

    /**
     * Restituisce il nome del personaggio.
     *
     * @return il nome del personaggio
     */
    public String getName() {
        return name;
    }

    /**
     * Restituisce il ruolo del personaggio.
     *
     * @return il ruolo del personaggio
     */
    public String getRole() {
        return role;
    }

    /**
     * Metodo astratto che ogni personaggio deve implementare
     * per definire come si presenta.
     */
    public abstract void presentati();

    @Override
    public String toString() {
        return name + " (" + role + ")";
    }
}
