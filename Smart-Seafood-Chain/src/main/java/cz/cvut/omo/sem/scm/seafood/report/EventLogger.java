package cz.cvut.omo.sem.scm.seafood.report;

import cz.cvut.omo.sem.scm.seafood.event.Event;
import cz.cvut.omo.sem.scm.seafood.event.EventListener;
import cz.cvut.omo.sem.scm.seafood.type.operation.EventType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Specialized Observer that records all system events.
 * Acts as a "Black Box" recorder for final report generation.
 * Thread-safe implementation using Collections.synchronizedList.
 */
public class EventLogger implements EventListener {

    // Thread-safe list wrapper.
    // We use ArrayList backed by synchronizedList for basic atomic operations (add,
    // get).
    private final List<Event> history = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void handleEvent(Event event) {
        // 1. Store event in memory (Atomic operation, thread-safe by default)
        history.add(event);

        // 2. Optional: Print significant events to console immediately (Live Log)
        logToConsole(event);
    }

    /**
     * Returns a DEFENSIVE COPY of the history.
     * Essential to avoid ConcurrentModificationException if the simulation
     * keeps adding events while the report generator is reading them.
     */
    public List<Event> getHistory() {
        // Must synchronize manually when iterating or copying a synchronizedList!
        synchronized (history) {
            return new ArrayList<>(history);
        }
    }

    /**
     * Filters history to find specific events safely.
     */
    public List<Event> getEventsByType(EventType type) {
        // Manual synchronization is required for Stream operations
        synchronized (history) {
            return history.stream()
                    .filter(e -> e.type() == type)
                    .collect(Collectors.toList());
        }
    }

    private void logToConsole(Event event) {
        // Truncate timestamp for cleaner console output
        String timeStr = event.timestamp().toString();
        if (timeStr.length() > 23) {
            timeStr = timeStr.substring(0, 23); // Cut off extra nanoseconds
        }
        System.out.printf("[LOG][%s] %s -> %s: %s%n",
                timeStr,
                event.sourceId(),
                event.type(),
                event.description());
    }
}