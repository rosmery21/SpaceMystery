package it.unicam.cs.mpgc.rpg126604.repository;

import it.unicam.cs.mpgc.rpg126604.interfaces.GameRepository;
import it.unicam.cs.mpgc.rpg126604.model.GameState;

public class InMemoryGameRepository implements GameRepository {

    private GameState savedState;

    @Override
    public void saveGame(GameState state) {
        this.savedState = state;
    }

    @Override
    public GameState loadGame() {
        return savedState;
    }

    @Override
    public boolean hasSavedGame() {
        return savedState != null;
    }
}