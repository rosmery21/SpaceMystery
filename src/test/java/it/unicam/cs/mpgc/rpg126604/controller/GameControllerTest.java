package it.unicam.cs.mpgc.rpg126604.controller;

import it.unicam.cs.mpgc.rpg126604.model.Clue;
import it.unicam.cs.mpgc.rpg126604.repository.InMemoryGameRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    private GameController controller;

    @BeforeEach
    void setUp() {
        controller = new GameController(new InMemoryGameRepository());
    }

    @Test
    void nuovaPartitaNonHaSalvataggi() {
        assertFalse(controller.hasSavedGame());
    }

    @Test
    void saveGameCreaUnSalvataggio() {
        controller.saveGame();

        assertTrue(controller.hasSavedGame());
    }

    @Test
    void spostarsiAumentaIlSospettoEAvanzaIlGiorno() {
        int sospettoIniziale = controller.getSuspicionLevel();
        int giorniIniziali = controller.getDaysRemaining();

        controller.moveToLocation("laboratorio");

        assertEquals(sospettoIniziale + 5,
                controller.getSuspicionLevel());

        assertEquals(giorniIniziali - 1,
                controller.getDaysRemaining());

        assertEquals("laboratorio",
                controller.getCurrentLocation().getId());
    }

    @Test
    void raccogliereIndiziLiAggiungeAlTaccuino() {
        controller.moveToLocation("laboratorio");

        Clue indizio = controller.getCurrentLocation()
                .getClues()
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new AssertionError(
                                "Nessun indizio presente nel laboratorio"
                        )
                );

        int indiziIniziali =
                controller.getAstronaut().getNotebook().size();

        controller.collectClue(indizio);

        assertEquals(indiziIniziali + 1,
                controller.getAstronaut().getNotebook().size());

        assertTrue(controller.getAstronaut()
                .getNotebook()
                .contains(indizio));
    }

    @Test
    void spostarsiVersoLuogoInesistenteLanciaEccezione() {
        assertThrows(
                IllegalArgumentException.class,
                () -> controller.moveToLocation("stanza_inesistente")
        );
    }
}