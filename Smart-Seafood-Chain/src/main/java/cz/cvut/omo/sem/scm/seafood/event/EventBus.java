package cz.cvut.omo.sem.scm.seafood.event;

import cz.cvut.omo.sem.scm.seafood.type.operation.EventType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBus {

    // --- SINGLETON IMPLEMENTATION ---
    private static volatile EventBus instance;

    private EventBus() { }

    public static EventBus getInstance() {
        if (instance == null) {
            synchronized (EventBus.class) {
                if (instance == null) {
                    instance = new EventBus();
                }
            }
        }
        return instance;
    }

    // Map: Event Type -> List of eager listeners (obeservers)
    private final Map<EventType, List<EventListener>> listeners = new HashMap<>();

    // 1. Subscribe
    public void subscribe(EventType type, EventListener listener) {
        listeners.computeIfAbsent(type, k -> new ArrayList<>()).add(listener);
    }

    // 2. Publish
    public void publish(Event event) {
        List<EventListener> subscribers = listeners.get(event.type());
        if (subscribers != null) {
            for (EventListener listener : subscribers) {
                listener.handleEvent(event);
            }
        }
    }

    /**
     * Clears all listeners.
     * Call this before starting a new simulation.
     */
    public void reset() {
        listeners.clear();
        System.out.println("[EventBus] Subscribers cleared.");
    }
}