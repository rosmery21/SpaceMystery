
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

    // Contiene tutti gli indizi, anche quelli ottenibili solo dai dialoghi.
    private final Map<String, Clue> clues;

    private final Map<String, NPC> npcs;
    private final Map<String, Quest> quests;

    private Location currentLocation;
    private int suspicionLevel;
    private int daysRemaining;

    // Membri dell'equipaggio con cui Alex ha completato un dialogo.
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
        this.clues = new LinkedHashMap<>();
        this.npcs = new LinkedHashMap<>();
        this.quests = new LinkedHashMap<>();

        this.suspicionLevel = 0;
        this.daysRemaining = MAX_DAYS;

        initializeGame();
    }

    private void initializeGame() {
        astronaut = new Astronaut("Alex");

        // Carica le stanze della stazione.
        for (Location location : dataLoader.loadLocations()) {
            locations.put(location.getId(), location);
        }

        // Registra tutti gli indizi, compresi quelli esclusivi dei dialoghi.
        for (Clue clue : dataLoader.loadClues()) {
            clues.put(clue.getId(), clue);

            // Solo gli indizi esplorabili appartengono a una stanza.
            if (clue.getLocationId() != null) {
                Location location = locations.get(clue.getLocationId());

                if (location != null) {
                    location.addClue(clue);
                }
            }
        }

        // Carica i membri dell'equipaggio.
        for (NPC npc : dataLoader.loadNPCs()) {
            npcs.put(npc.getId(), npc);
        }

        // Carica le missioni di Alex.
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
        saveGame();
    }

    // Raccoglie un indizio e salva i progressi della missione.
    public void collectClue(Clue clue) {
        if (clue == null) {
            throw new IllegalArgumentException("Indizio non valido");
        }

        // Evita di aggiungere due volte lo stesso indizio al taccuino.
        if (astronaut.getNotebook().contains(clue)) {
            return;
        }

        astronaut.addClueToNotebook(clue);
        updateClueObjectives(clue);

        // Salva immediatamente l'indizio raccolto.
        saveGame();
    }

    public Report createReport(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Titolo non valido");
        }

        Report report = new Report(
                "r" + System.currentTimeMillis(),
                title
        );

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

    // Gestisce le risposte e gli indizi esclusivi dei dialoghi.
    public String selectDialogueChoice(String npcId, DialogueChoice choice) {
        NPC npc = npcs.get(npcId);

        if (npc == null) {
            throw new IllegalArgumentException("NPC non trovato");
        }

        if (choice == null || !npc.getDialogueChoices().contains(choice)) {
            throw new IllegalArgumentException(
                    "Scelta di dialogo non valida"
            );
        }

        // Alex deve avere abbastanza carisma per scegliere la risposta.
        if (!canSelectDialogueChoice(choice)) {
            increaseSuspicion(5);

            throw new IllegalStateException(
                    "Carisma insufficiente per questa risposta. "
                            + "Il membro dell'equipaggio ti guarda con sospetto."
            );
        }

        // Assegna la ricompensa prevista dal dialogo.
        astronaut.getStats().addExperience(
                choice.getExperienceReward()
        );

        decreaseSuspicion(5);

        // Se il dialogo rivela un indizio, lo aggiunge al taccuino.
        if (choice.hasClue()) {
            Clue clue = findClueById(choice.getClueId());

            if (clue != null) {
                collectClue(clue);
            }
        }

        contactedNPCs.add(npcId);
        updateDialogueObjectives(npcId);

        saveGame();

        return choice.getResponse();
    }

    // Cerca un indizio tra tutti quelli caricati dal JSON.
    // Funziona anche quando locationId è null.
    private Clue findClueById(String clueId) {
        return clues.get(clueId);
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

    // Aggiorna le missioni soltanto per gli indizi esplorabili.
    private void updateClueObjectives(Clue clue) {

        // Gli indizi dei dialoghi non appartengono a una stanza.
        if (clue.getLocationId() == null) {
            return;
        }

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

    public int getSuspicionLevel() {
        return suspicionLevel;
    }

    public int getDaysRemaining() {
        return daysRemaining;
    }

    public int getMaxSuspicion() {
        return MAX_SUSPICION;
    }

    public int getMaxDays() {
        return MAX_DAYS;
    }

    public void increaseSuspicion(int amount) {
        this.suspicionLevel = Math.min(
                suspicionLevel + amount,
                MAX_SUSPICION
        );
    }

    public void decreaseSuspicion(int amount) {
        this.suspicionLevel = Math.max(
                suspicionLevel - amount,
                0
        );
    }

    public void advanceDay() {
        if (daysRemaining > 0) {
            daysRemaining--;
        }
    }

    public boolean isGameOver() {
        return suspicionLevel >= MAX_SUSPICION
                || daysRemaining <= 0;
    }

    public boolean isVictory() {
        return astronaut.getReputation() >= 100;
    }

    public String getGameOverReason() {
        if (suspicionLevel >= MAX_SUSPICION) {
            return "Il livello di sospetto è troppo alto: "
                    + "l'equipaggio ti ha scoperto!";
        }

        if (daysRemaining <= 0) {
            return "Il tempo per completare la missione è scaduto!";
        }

        return "";
    }

    /**
     * Suggerisce il prossimo passo della missione
     * senza modificare lo stato di gioco.
     */
    public String getCurrentObjective() {

        // Mostra il primo obiettivo ancora da completare.
        for (Quest quest : astronaut.getActiveQuests()) {
            List<String> objectives = quest.getObjectives();

            for (int i = 0; i < objectives.size(); i++) {
                if (!quest.isObjectiveCompleted(i)) {
                    return objectives.get(i);
                }
            }
        }

        // La missione principale termina con 100 reputazione.
        int reputation = astronaut.getReputation();

        if (reputation < 100) {
            return reputation == 0
                    ? "Prepara e pubblica il primo rapporto!"
                    : "Pubblica altri rapporti! ("
                    + reputation
                    + "/100 reputazione)";
        }

        return "MISSIONE COMPLETATA!";
    }

    // Salva i dati necessari a riprendere la missione.
    public void saveGame() {
        GameState state = new GameState(
                astronaut,
                currentLocation.getId(),
                suspicionLevel,
                daysRemaining,
                contactedNPCs
        );

        repository.saveGame(state);
    }

    // Verifica se esiste una partita salvata.
    public boolean hasSavedGame() {
        return repository.hasSavedGame();
    }

    // Carica una partita salvata e ripristina i progressi di Alex.
    public boolean loadGame() {
        if (!repository.hasSavedGame()) {
            return false;
        }

        GameState state = repository.loadGame();

        if (state == null || state.getAstronaut() == null) {
            return false;
        }

        Astronaut savedAstronaut = state.getAstronaut();

        // Verifica che la stanza salvata esista ancora.
        Location savedLocation = locations.get(
                state.getCurrentLocationId()
        );

        if (savedLocation == null) {
            savedLocation = locations.get("sala_comandi");
        }

        if (savedLocation == null) {
            throw new IllegalStateException(
                    "La Sala Comandi non è presente nei dati del gioco"
            );
        }

        // Ripristina statistiche, rapporti e reputazione.
        this.astronaut = savedAstronaut;
        this.currentLocation = savedLocation;

        // Ripristina il livello di sospetto.
        this.suspicionLevel = state.getSuspicionLevel();

        // Ripristina i giorni disponibili.
        this.daysRemaining = state.getDaysRemaining();

        // Ripristina i membri dell'equipaggio già contattati.
        this.contactedNPCs.clear();
        this.contactedNPCs.addAll(state.getContactedNPCs());

        // Riallinea gli indizi raccolti con quelli caricati dal JSON.
        for (Clue savedClue : astronaut.getNotebook()) {
            Clue originalClue = findClueById(savedClue.getId());

            if (originalClue != null) {
                originalClue.discover();
            }
        }

        // Riallinea le stanze visitate.
        for (Location savedVisitedLocation :
                astronaut.getVisitedLocations()) {

            Location originalLocation = locations.get(
                    savedVisitedLocation.getId()
            );

            if (originalLocation != null) {
                originalLocation.visit();
            }
        }

        // Riallinea lo stato delle missioni.
        for (Quest savedQuest : astronaut.getActiveQuests()) {
            restoreQuestProgress(savedQuest);
        }

        for (Quest savedQuest : astronaut.getCompletedQuests()) {
            restoreQuestProgress(savedQuest);
        }

        return true;
    }

    // Ripristina gli obiettivi senza assegnare nuove ricompense.
    private void restoreQuestProgress(Quest savedQuest) {
        Quest originalQuest = quests.get(savedQuest.getId());

        if (originalQuest == null) {
            return;
        }

        int objectivesCount = Math.min(
                originalQuest.getTotalObjectivesCount(),
                savedQuest.getTotalObjectivesCount()
        );

        for (int i = 0; i < objectivesCount; i++) {
            if (savedQuest.isObjectiveCompleted(i)) {
                originalQuest.completeObjective(i);
            }
        }
    }
}
