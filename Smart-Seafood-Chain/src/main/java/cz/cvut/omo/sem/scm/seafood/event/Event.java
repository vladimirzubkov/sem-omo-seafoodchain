package cz.cvut.omo.sem.scm.seafood.event;

import cz.cvut.omo.sem.scm.seafood.type.operation.EventType;

import java.time.LocalDateTime;

/**
 * Represents a discrete, immutable event in the simulation system.
 * Implemented as a Java Record for conciseness and safety.
 */
public record Event(LocalDateTime timestamp, EventType type, String sourceId, String targetId, String description,
                    Object payload) {

    /**
     * Compact constructor ensures the timestamp defaults to 'now' if not provided
     * (used by the separate EventBuilder class).
     */
    public Event {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
}