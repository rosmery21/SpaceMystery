
package it.unicam.cs.mpgc.rpg126604.model;

import it.unicam.cs.mpgc.rpg126604.interfaces.Identifiable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NPC extends GameCharacter implements Identifiable {

    private String id;
    private final List<String> dialogues;
    private final List<Clue> cluesProvided;

    // Costruttore vuoto per la deserializzazione JSON
    public NPC() {
        super();
        this.dialogues = new ArrayList<>();
        this.cluesProvided = new ArrayList<>();
    }

    public NPC(String id, String name, String role) {
        super(name, role);

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID non valido");
        }

        this.id = id;
        this.dialogues = new ArrayList<>();
        this.cluesProvided = new ArrayList<>();
    }

    @Override
    public void presentati() {
        System.out.println("Sono " + getName()
                + ", " + getRole() + " della stazione spaziale.");
    }

    @Override
    public String getId() {
        return id;
    }

    public void addDialogue(String dialogue) {
        if (dialogue == null || dialogue.isBlank()) {
            throw new IllegalArgumentException("Dialogo non valido");
        }

        dialogues.add(dialogue);
    }

    public void addClue(Clue clue) {
        if (clue == null) {
            throw new IllegalArgumentException("Indizio non valido");
        }

        if (!cluesProvided.contains(clue)) {
            cluesProvided.add(clue);
        }
    }

    public List<String> getDialogues() {
        return Collections.unmodifiableList(dialogues);
    }

    public List<Clue> getCluesProvided() {
        return Collections.unmodifiableList(cluesProvided);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof NPC)) return false;

        NPC other = (NPC) obj;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
