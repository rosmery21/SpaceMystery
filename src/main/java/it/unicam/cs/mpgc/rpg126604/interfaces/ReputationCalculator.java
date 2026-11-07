
package it.unicam.cs.mpgc.rpg126604.interfaces;

import it.unicam.cs.mpgc.rpg126604.model.Report;

@FunctionalInterface
public interface ReputationCalculator {

    int calculate(Report report);
}
