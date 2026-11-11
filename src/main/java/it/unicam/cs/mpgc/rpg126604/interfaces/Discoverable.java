package it.unicam.cs.mpgc.rpg126604.interfaces;

// contratto per gli elementi che possono essere scoperti
public interface Discoverable {

    boolean isDiscovered();

    void discover();

    // restituisce lo stato della scoperta in modo leggibile
    // tutte le classi che implementano Discoverable
    // possono usare questo metodo senza riscriverlo
    default String discoveryStatus() {
        return isDiscovered() ? "scoperto" : "non scoperto";
    }
}