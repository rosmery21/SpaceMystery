
package it.unicam.cs.mpgc.rpg126604.model;

/**
 * Rappresenta le statistiche dell'astronauta Alex.
 * Le statistiche influenzano le meccaniche di gioco:
 * - Intelligenza: capacità investigative
 * - Carisma: possibilità di dialogo con gli NPC
 * - Furtività: accesso ad aree riservate
 */
// abilità e punti esperienza dell’astronauta
public class PlayerStats {

    private int intelligence;
    private int charisma;
    private int stealth;
    private int level;
    private int experience;

    private static final int XP_PER_LEVEL = 100;

    public PlayerStats() {
        this.intelligence = 5;
        this.charisma = 5;
        this.stealth = 5;
        this.level = 1;
        this.experience = 0;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getCharisma() {
        return charisma;
    }

    public int getStealth() {
        return stealth;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public int getXpToNextLevel() {
        return (level * XP_PER_LEVEL) - experience;
    }

    /**
     * Aggiunge esperienza all'astronauta.
     * Quando viene raggiunta la soglia prevista,
     * il personaggio sale di livello e migliora le statistiche.
     *
     * @param amount quantità di esperienza da aggiungere
     */
    public void addExperience(int amount) {
        if (amount <= 0) {
            return;
        }

        this.experience += amount;

        while (experience >= level * XP_PER_LEVEL) {
            levelUp();
        }
    }

    private void levelUp() {
        this.level++;
        this.intelligence++;
        this.charisma++;
        this.stealth++;
    }

    /**
     * Verifica se l'astronauta può accedere
     * a un'area riservata in base alla furtività.
     *
     * @param requiredStealth furtività richiesta
     * @return true se l'accesso è consentito
     */
    public boolean canAccess(int requiredStealth) {
        return this.stealth >= requiredStealth;
    }

    /**
     * Verifica se l'astronauta può utilizzare
     * una scelta di dialogo in base al carisma.
     *
     * @param requiredCharisma carisma richiesto
     * @return true se la scelta è disponibile
     */
    public boolean canUseDialogueOption(int requiredCharisma) {
        return this.charisma >= requiredCharisma;
    }

    @Override
    public String toString() {
        return String.format(
                "LV.%d | INT:%d | CAR:%d | FUR:%d | XP:%d/%d",
                level, intelligence, charisma, stealth,
                experience, level * XP_PER_LEVEL
        );
    }
}
