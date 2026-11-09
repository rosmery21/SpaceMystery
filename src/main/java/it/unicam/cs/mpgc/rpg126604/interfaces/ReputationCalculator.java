
package it.unicam.cs.mpgc.rpg126604.interfaces;

import it.unicam.cs.mpgc.rpg126604.model.Report;

/**
 * Interfaccia funzionale per il calcolo della reputazione
 * ottenuta attraverso la pubblicazione di un rapporto
 * investigativo in SpaceMystery.
 *
 * Essendo annotata con {@link FunctionalInterface},
 * può essere implementata tramite espressioni lambda.
 *
 * Questo permette di modificare la strategia di calcolo
 * della reputazione senza cambiare il codice esistente,
 * rispettando il principio OCP (Open/Closed Principle).
 *
 * @version 1.0
 */
@FunctionalInterface
public interface ReputationCalculator {

    /**
     * Calcola il valore della reputazione associato
     * a un rapporto investigativo.
     *
     * @param report il rapporto da utilizzare per il calcolo
     * @return il valore della reputazione calcolata
     */
    int calculate(Report report);
}
