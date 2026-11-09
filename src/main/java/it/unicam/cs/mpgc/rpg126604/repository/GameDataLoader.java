
package it.unicam.cs.mpgc.rpg126604.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unicam.cs.mpgc.rpg126604.model.Clue;
import it.unicam.cs.mpgc.rpg126604.model.Location;
import it.unicam.cs.mpgc.rpg126604.model.NPC;
import it.unicam.cs.mpgc.rpg126604.model.Quest;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Carica i dati di SpaceMystery dai file JSON
 * presenti nelle risorse del progetto.
 */
public class GameDataLoader {

    private final Gson gson;

    public GameDataLoader() {
        this.gson = new Gson();
    }

    /**
     * Carica le ambientazioni della stazione spaziale.
     *
     * @return la lista delle ambientazioni
     */
    public List<Location> loadLocations() {
        Type type = new TypeToken<List<Location>>() {}.getType();
        return loadFromJson("/locations.json", type);
    }

    /**
     * Carica gli indizi investigativi.
     *
     * @return la lista degli indizi
     */
    public List<Clue> loadClues() {
        Type type = new TypeToken<List<Clue>>() {}.getType();
        return loadFromJson("/clues.json", type);
    }

    /**
     * Carica gli NPC e i relativi dati di dialogo.
     *
     * @return la lista dei personaggi non giocanti
     */
    public List<NPC> loadNPCs() {
        Type type = new TypeToken<List<NPC>>() {}.getType();
        return loadFromJson("/npcs.json", type);
    }

    /**
     * Carica le missioni definite in quests.json.
     *
     * @return la lista delle missioni
     */
    public List<Quest> loadQuests() {
        Type type = new TypeToken<List<Quest>>() {}.getType();
        return loadFromJson("/quests.json", type);
    }

    /**
     * Legge e deserializza un file JSON dalle risorse.
     *
     * @param fileName il percorso del file JSON
     * @param type il tipo dei dati da deserializzare
     * @param <T> il tipo del risultato
     * @return i dati caricati
     */
    private <T> T loadFromJson(String fileName, Type type) {
        InputStream inputStream =
                getClass().getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalStateException(
                    "File JSON non trovato: " + fileName
            );
        }

        try (Reader reader = new InputStreamReader(
                inputStream, StandardCharsets.UTF_8)) {

            return gson.fromJson(reader, type);

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Errore durante il caricamento di " + fileName, e
            );
        }
    }
}
