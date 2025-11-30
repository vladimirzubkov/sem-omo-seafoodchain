package cz.cvut.omo.sem.scm.seafood.pattern.memento;

import java.util.Stack;

/**
 * Caretaker class.
 * Manages the history of mementos.
 */
public class Caretaker {

    private final Stack<SimulationMemento> history = new Stack<>();

    public void save(SimulationMemento memento) {
        history.push(memento);
        System.out.println("Simulation state saved at tick: %d".formatted(memento.tick()));
    }

    public SimulationMemento undo() {
        if (!history.isEmpty()) {
            SimulationMemento memento = history.pop();
            System.out.println("Restoring simulation to tick: %d".formatted(memento.tick()));
            return memento;
        }
        return null;
    }
}