package cz.cvut.omo.sem.scm.seafood.pattern.memento;

import cz.cvut.omo.sem.scm.seafood.model.SimulationEntity;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Memento Pattern.
 * Stores the internal state of the Simulator at a specific tick.
 * Immutable object.
 */
public record SimulationMemento(
        int tick,
        LocalDateTime timestamp,
        List<SimulationEntity> entitiesSnapshot // Deep copy of entities
) {
    // Constructor handles the data capture
}