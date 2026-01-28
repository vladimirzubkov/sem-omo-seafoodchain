package cz.cvut.omo.sem.scm.seafood.model.employee;

import cz.cvut.omo.sem.scm.seafood.event.Event;
import cz.cvut.omo.sem.scm.seafood.event.EventBus;
import cz.cvut.omo.sem.scm.seafood.event.EventListener;
import cz.cvut.omo.sem.scm.seafood.model.SimulationEntity;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.pattern.builder.EventBuilder;
import cz.cvut.omo.sem.scm.seafood.model.employee.role.JobRole;
import cz.cvut.omo.sem.scm.seafood.pattern.visitor.EntityVisitor;
import cz.cvut.omo.sem.scm.seafood.pattern.visitor.Visitable;
import cz.cvut.omo.sem.scm.seafood.resource.Money;
import cz.cvut.omo.sem.scm.seafood.simulation.Time; // Dependency on Time utility
import cz.cvut.omo.sem.scm.seafood.type.operation.EventType;
import cz.cvut.omo.sem.scm.seafood.type.role.LaborRoleType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Represents a worker in the supply chain.
 * Acts as a container for JobRoles (Strategy Pattern) and handles Events (Observer Pattern).
 */
@Getter
@Setter
public class Employee extends SimulationEntity implements EventListener, Visitable {

    private Party employer; // employee works for a party
    private Money salaryPerHour;
    private int shiftStartHour;     // e.g., 9 for 09:00
    private int shiftDurationHours; // e.g., 8 hours
    private EventBus eventBus;

    private List<JobRole> roles = new ArrayList<>();
    private final Queue<Event> inbox = new LinkedList<>();

    // State to track shift transitions (Clock In / Clock Out)
    private boolean wasOnShiftLastTick = false;

    public Employee(String id, String name, Money salary) {
        super(id, name);
        this.salaryPerHour = salary;
    }

    @Override
    public void accept(EntityVisitor visitor) {
        visitor.visit(this);
    }

    public void addRole(JobRole role) {
        roles.add(role);
    }

    /**
     * Helper method for Roles to publish events (e.g. "Repair Finished").
     */
    public void reportAction(EventType type, String description, Object payload) {
        if (eventBus != null) {
            // Using the explicit EventBuilder pattern class
            Event event = new EventBuilder()
                    .type(type)
                    .sourceId(this.getId())
                    .description(description)
                    .payload(payload)
                    .build();
            eventBus.publish(event);
        }
    }

    /**
     * Main simulation loop method.
     */
    @Override
    public void handleTick() {
        // 1. Process incoming events regardless of shift
        processInbox();

        // 2. Check Shift Status
        boolean currentlyOnShift = checkIsOnShift();

        // 3. Handle Shift Transitions (Fire SHIFT_STARTED / SHIFT_ENDED events)
        if (currentlyOnShift && !wasOnShiftLastTick) {
            reportAction(EventType.SHIFT_STARTED, "Started shift at %d".formatted(Time.getCurrentHour()), null);
        } else if (!currentlyOnShift && wasOnShiftLastTick) {
            reportAction(EventType.SHIFT_ENDED, "Ended shift at %d".formatted(Time.getCurrentHour()), null);
        }
        wasOnShiftLastTick = currentlyOnShift;

        // 4. If off-duty, stop execution
        if (!currentlyOnShift) {
            return;
        }

        // 5. Log Cost (Salary) - fulfillment of FRQ12 (placeholder)
        // context.getParty().recordCost(salaryPerHour);

        // 6. Delegate work to assigned roles
        for (JobRole role : roles) {
            role.work(this);
        }
    }

    /**
     * Handles incoming events from the EventBus (Observer Pattern).
     * Filters events relevant to this specific employee.
     */
    @Override
    public void handleEvent(Event event) {
        // FIX: Accessing record fields directly via method call
        if (event.targetId() == null || event.targetId().equals(this.getId())) {
            inbox.add(event);
        }
    }

    private void processInbox() {
        while (!inbox.isEmpty()) {
            Event event = inbox.poll();

            // Strategy Pattern Delegation.
            // The Employee doesn't need to know IF it's a breakdown or a fire.
            // The Role decides whether to react.
            for (JobRole role : roles) {
                role.onEvent(event, this);
            }
        }
    }

    /**
     * Calculates if the employee is currently working based on Simulation Time.
     * Uses Time.getCurrentHour() from the global simulation clock.
     */
    private boolean checkIsOnShift() {
        int currentHour = Time.getCurrentHour();
        int endHour = (shiftStartHour + shiftDurationHours) % 24;

        if (shiftStartHour < endHour) {
            // Day shift (e.g., 09:00 to 17:00)
            return currentHour >= shiftStartHour && currentHour < endHour;
        } else {
            // Overnight shift (e.g., 22:00 to 06:00)
            return currentHour >= shiftStartHour || currentHour < endHour;
        }
    }
}