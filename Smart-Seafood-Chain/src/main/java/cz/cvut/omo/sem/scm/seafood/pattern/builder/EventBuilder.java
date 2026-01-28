package cz.cvut.omo.sem.scm.seafood.pattern.builder;

import cz.cvut.omo.sem.scm.seafood.event.Event;
import cz.cvut.omo.sem.scm.seafood.type.operation.EventType;
import java.time.LocalDateTime;

/**
 * Builder Pattern implementation for Event class.
 * Separated from the model class to strictly follow the pattern structure.
 * Provides fluent interface for event building, checks for data validity (nulls, etc.) at the final step .build,
 * and returns an immutable Event object (record), (note: this is 'shallow immutability' - content of the payload may change,
 * as it accepts Object type).
 */
public class EventBuilder {

    private LocalDateTime timestamp = LocalDateTime.now(); // Default value
    private EventType type;
    private String sourceId;
    private String targetId;
    private String description;
    private Object payload;

    // --- Fluent Interface Setters ---

    public EventBuilder type(EventType type) {
        this.type = type;
        return this;
    }

    public EventBuilder sourceId(String sourceId) {
        this.sourceId = sourceId;
        return this;
    }

    public EventBuilder targetId(String targetId) {
        this.targetId = targetId;
        return this;
    }

    public EventBuilder description(String description) {
        this.description = description;
        return this;
    }

    // note that here content of the object may change
    public EventBuilder payload(Object payload) {
        this.payload = payload;
        return this;
    }

    public EventBuilder timestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /**
     * Constructs the Event object after validating inputs.
     */
    public Event build() {
        if (type == null) {
            throw new IllegalStateException("Event Type is required");
        }
        if (sourceId == null) {
            throw new IllegalStateException("Source ID is required");
        }
        return new Event(timestamp, type, sourceId, targetId, description, payload);
    }
}