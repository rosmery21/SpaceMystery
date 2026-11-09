
package it.unicam.cs.mpgc.rpg126604.model;

/**
 * Rappresenta una scelta di dialogo con un NPC.
 * Ogni scelta può richiedere un livello minimo di carisma
 * e può fornire indizi o punti esperienza all'astronauta.
 */
public class DialogueChoice {

    private final String text;
    private final String response;
    private final int requiredCharisma;
    private final String clueId;
    private final int experienceReward;

    public DialogueChoice() {
        this.text = "";
        this.response = "";
        this.requiredCharisma = 0;
        this.clueId = null;
        this.experienceReward = 0;
    }

    public DialogueChoice(String text, String response,
                          int requiredCharisma, String clueId,
                          int experienceReward) {

        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Testo non valido");
        }

        if (response == null || response.isEmpty()) {
            throw new IllegalArgumentException("Risposta non valida");
        }

        this.text = text;
        this.response = response;
        this.requiredCharisma = requiredCharisma;
        this.clueId = clueId;
        this.experienceReward = experienceReward;
    }

    public String getText() {
        return text;
    }

    public String getResponse() {
        return response;
    }

    public int getRequiredCharisma() {
        return requiredCharisma;
    }

    public String getClueId() {
        return clueId;
    }

    public int getExperienceReward() {
        return experienceReward;
    }

    public boolean hasClue() {
        return clueId != null && !clueId.isEmpty();
    }
}
