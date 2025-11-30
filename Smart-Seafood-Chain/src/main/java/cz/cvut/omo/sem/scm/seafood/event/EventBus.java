package cz.cvut.omo.sem.scm.seafood.event;

import cz.cvut.omo.sem.scm.seafood.type.operation.EventType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBus {
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
}