package cz.cvut.omo.sem.scm.seafood.model;

import lombok.*;

/**
 * Base class for all active entities in the simulation.
 * Fixes: Allows SimulationController to treat Parties, Employees, and Machines uniformly.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public abstract class SimulationEntity {
    private final String id;
    private String name;

    public SimulationEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // The main heartbeat method.
    // SimulationController calls this, and the entity decides what to do based on its Roles/State.
    public abstract void handleTick();
}