
package it.unicam.cs.mpgc.rpg126604.interfaces;

import it.unicam.cs.mpgc.rpg126604.model.GameState;

public interface GameRepository {

    void saveGame(GameState state);

    GameState loadGame();

    boolean hasSavedGame();
}
