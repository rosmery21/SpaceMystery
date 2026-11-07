
package it.unicam.cs.mpgc.rpg126604.model;

public abstract class GameCharacter {

    private String name;
    private String role;

    public GameCharacter() {
    }

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

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public abstract void presentati();

    @Override
    public String toString() {
        return name + " (" + role + ")";
    }
}
