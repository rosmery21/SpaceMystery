
package it.unicam.cs.mpgc.rpg126604.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unicam.cs.mpgc.rpg126604.model.Clue;
import it.unicam.cs.mpgc.rpg126604.model.Location;
import it.unicam.cs.mpgc.rpg126604.model.NPC;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GameDataLoader {

    private final Gson gson;

    public GameDataLoader() {
        this.gson = new Gson();
    }

    public List<Location> loadLocations() {
        Type type = new TypeToken<List<Location>>() {}.getType();
        return loadFromJson("/locations.json", type);
    }

    public List<Clue> loadClues() {
        Type type = new TypeToken<List<Clue>>() {}.getType();
        return loadFromJson("/clues.json", type);
    }

    public List<NPC> loadNPCs() {
        Type type = new TypeToken<List<NPC>>() {}.getType();
        return loadFromJson("/npcs.json", type);
    }

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
