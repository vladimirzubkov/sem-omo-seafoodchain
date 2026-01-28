package cz.cvut.omo.sem.scm.seafood.pattern.memento;

import cz.cvut.omo.sem.scm.seafood.model.SimulationEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Collections;

/**
 * Memento record representing a snapshot of the simulation world.
 * Immutable.
 */
public record SimulationMemento(
        int tick,
        LocalDateTime timestamp,
        List<SimulationEntity> entitiesSnapshot
) {
    public SimulationMemento {
        // Defensive copy to ensure immutability of the list itself
        entitiesSnapshot = Collections.unmodifiableList(entitiesSnapshot);
    }
}