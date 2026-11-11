package it.unicam.cs.mpgc.rpg126604.controller;

import it.unicam.cs.mpgc.rpg126604.interfaces.GameRepository;
import it.unicam.cs.mpgc.rpg126604.model.*;
import it.unicam.cs.mpgc.rpg126604.repository.GameDataLoader;

import java.util.*;

// coordina esplorazione, indizi, dialoghi e missioni della stazione
public class GameController {

    private final GameRepository repository;
    private final GameDataLoader dataLoader;

    private Astronaut astronaut;
    private final Map<String, Location> locations;
    private final Map<String, NPC> npcs;
    private final Map<String, Quest> quests;
    private Location currentLocation;
    private int suspicionLevel;
    private int daysRemaining;
    // Membri dell’equipaggio con cui Alex ha completato un dialogo.
    private final Set<String> contactedNPCs = new HashSet<>();
    private static final int MAX_SUSPICION = 100;
    private static final int MAX_DAYS = 10;

    public GameController(GameRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository non valido");
        }

        this.repository = repository;
        this.dataLoader = new GameDataLoader();
        this.locations = new LinkedHashMap<>();
        this.npcs = new LinkedHashMap<>();
        this.quests = new LinkedHashMap<>();
        this.suspicionLevel = 0;
        this.daysRemaining = MAX_DAYS;

        initializeGame();
    }

    private void initializeGame() {
        astronaut = new Astronaut("Alex");

        for (Location location : dataLoader.loadLocations()) {
            locations.put(location.getId(), location);
        }

        for (Clue clue : dataLoader.loadClues()) {
            Location location = locations.get(clue.getLocationId());
            if (location != null) {
                location.addClue(clue);
            }
        }

        for (NPC npc : dataLoader.loadNPCs()) {
            npcs.put(npc.getId(), npc);
        }

        for (Quest quest : dataLoader.loadQuests()) {
            quests.put(quest.getId(), quest);
            astronaut.addQuest(quest);
        }

        currentLocation = locations.get("sala_comandi");
        if (currentLocation == null) {
            throw new IllegalStateException(
                    "La Sala Comandi non è presente in locations.json"
            );
        }

        astronaut.visitLocation(currentLocation);
        updateLocationObjectives(currentLocation);
    }

    public Astronaut getAstronaut() {
        return astronaut;
    }

    public GameStats getGameStats() {
        return new GameStats(astronaut);
    }

    public PlayerStats getPlayerStats() {
        return astronaut.getStats();
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public Collection<Location> getAllLocations() {
        return Collections.unmodifiableCollection(locations.values());
    }

    public Collection<NPC> getAllNPCs() {
        return Collections.unmodifiableCollection(npcs.values());
    }

    public Collection<Quest> getAllQuests() {
        return Collections.unmodifiableCollection(quests.values());
    }

    public List<Quest> getActiveQuests() {
        return astronaut.getActiveQuests();
    }

    public List<Quest> getCompletedQuests() {
        return astronaut.getCompletedQuests();
    }

    public void moveToLocation(String locationId) {
        Location location = locations.get(locationId);
        if (location == null) {
            throw new IllegalArgumentException("Stanza non trovata");
        }

        currentLocation = location;
        astronaut.visitLocation(location);
        increaseSuspicion(5);
        advanceDay();
        updateLocationObjectives(location);
    }

    public void collectClue(Clue clue) {
        if (clue == null) {
            throw new IllegalArgumentException("Indizio non valido");
        }

        astronaut.addClueToNotebook(clue);
        updateClueObjectives(clue);
    }

    public Report createReport(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Titolo non valido");
        }

        Report report = new Report("r" + System.currentTimeMillis(), title);
        astronaut.addReport(report);
        return report;
    }

    public void publishReport(Report report) {
        astronaut.publishReport(report);
        decreaseSuspicion(15);
        saveGame();
    }

    public NPC getNPC(String npcId) {
        return npcs.get(npcId);
    }

    public List<DialogueChoice> getDialogueChoices(String npcId) {
        NPC npc = npcs.get(npcId);
        if (npc == null) {
            throw new IllegalArgumentException("NPC non trovato");
        }
        return npc.getDialogueChoices();
    }

    public boolean canSelectDialogueChoice(DialogueChoice choice) {
        if (choice == null) {
            return false;
        }
        return astronaut.getStats()
                .canUseDialogueOption(choice.getRequiredCharisma());
    }

    public String selectDialogueChoice(String npcId, DialogueChoice choice) {
        NPC npc = npcs.get(npcId);
        if (npc == null) {
            throw new IllegalArgumentException("NPC non trovato");
        }
        if (choice == null || !npc.getDialogueChoices().contains(choice)) {
            throw new IllegalArgumentException("Scelta di dialogo non valida");
        }
        if (!canSelectDialogueChoice(choice)) {
            increaseSuspicion(5);
            throw new IllegalStateException(
                    "Carisma insufficiente per questa risposta. "
                            + "Il membro dell'equipaggio ti guarda con sospetto."
            );
        }

        astronaut.getStats().addExperience(choice.getExperienceReward());
        decreaseSuspicion(5);

        if (choice.hasClue()) {
            Clue clue = findClueById(choice.getClueId());
            if (clue != null) {
                collectClue(clue);
            }
        }

        contactedNPCs.add(npcId);
        updateDialogueObjectives(npcId);
        return choice.getResponse();
    }

    private Clue findClueById(String clueId) {
        for (Location location : locations.values()) {
            for (Clue clue : location.getClues()) {
                if (clue.getId().equals(clueId)) {
                    return clue;
                }
            }
        }
        return null;
    }

    private void updateLocationObjectives(Location location) {
        String objective = switch (location.getId()) {
            case "sala_comandi" -> "Esplora la Sala Comandi";
            case "laboratorio" -> "Visita il Laboratorio Scientifico";
            case "sala_motori" -> "Esplora la Sala Motori";
            default -> null;
        };

        if (objective != null) {
            completeMatchingObjectives(objective);
        }
    }

    private void updateClueObjectives(Clue clue) {
        String objective = switch (clue.getLocationId()) {
            case "sala_comandi" -> "Raccogli un indizio sul segnale";
            case "laboratorio" -> "Raccogli un indizio scientifico";
            case "sala_motori" -> "Raccogli un indizio sui malfunzionamenti";
            default -> null;
        };

        if (objective != null) {
            completeMatchingObjectives(objective);
        }
    }

    private void updateDialogueObjectives(String npcId) {
        String objective = switch (npcId) {
            case "npc_vega" -> "Parla con Vega";
            case "npc_nova" -> "Parla con Nova";
            case "npc_orion" -> "Parla con Orion";
            default -> null;
        };

        if (objective != null) {
            completeMatchingObjectives(objective);
        }
    }

    private void completeMatchingObjectives(String objective) {
        List<Quest> activeQuests =
                new ArrayList<>(astronaut.getActiveQuests());

        for (Quest quest : activeQuests) {
            List<String> objectives = quest.getObjectives();

            for (int i = 0; i < objectives.size(); i++) {
                if (objectives.get(i).equals(objective)
                        && !quest.isObjectiveCompleted(i)) {
                    quest.completeObjective(i);
                }
            }

            if (quest.isCompleted()) {
                astronaut.completeQuest(quest);
            }
        }
    }

    public int getSuspicionLevel() { return suspicionLevel; }
    public int getDaysRemaining() { return daysRemaining; }
    public int getMaxSuspicion() { return MAX_SUSPICION; }
    public int getMaxDays() { return MAX_DAYS; }

    public void increaseSuspicion(int amount) {
        this.suspicionLevel = Math.min(suspicionLevel + amount, MAX_SUSPICION);
    }

    public void decreaseSuspicion(int amount) {
        this.suspicionLevel = Math.max(suspicionLevel - amount, 0);
    }

    public void advanceDay() {
        if (daysRemaining > 0) daysRemaining--;
    }

    public boolean isGameOver() {
        return suspicionLevel >= MAX_SUSPICION || daysRemaining <= 0;
    }

    public boolean isVictory() {
        return astronaut.getReputation() >= 100;
    }

    public String getGameOverReason() {
        if (suspicionLevel >= MAX_SUSPICION)
            return "Il livello di sospetto è troppo alto: l'equipaggio ti ha scoperto!";
        if (daysRemaining <= 0)
            return "Il tempo per completare la missione è scaduto!";
        return "";
    }

    /**
     * Suggerisce il prossimo passo della missione senza modificare lo stato di gioco.
     */
    public String getCurrentObjective() {
        // Mostra il primo obiettivo ancora da completare delle missioni attive.
        for (Quest quest : astronaut.getActiveQuests()) {
            List<String> objectives = quest.getObjectives();
            for (int i = 0; i < objectives.size(); i++) {
                if (!quest.isObjectiveCompleted(i)) {
                    return objectives.get(i);
                }
            }
        }

        // La missione principale non è conclusa finché la reputazione
        // non raggiunge la soglia di vittoria.
        int reputation = astronaut.getReputation();
        if (reputation < 100) {
            return reputation == 0
                    ? "Prepara e pubblica il primo rapporto!"
                    : "Pubblica altri rapporti! (" + reputation + "/100 reputazione)";
        }

        return "MISSIONE COMPLETATA!";
    }

    public void saveGame() {
        GameState state = new GameState(astronaut, currentLocation.getId());
        repository.saveGame(state);
    }

    public boolean hasSavedGame() {
        return repository.hasSavedGame();
    }
}


