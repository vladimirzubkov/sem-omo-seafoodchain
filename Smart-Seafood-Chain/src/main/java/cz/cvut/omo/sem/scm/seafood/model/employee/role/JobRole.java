package cz.cvut.omo.sem.scm.seafood.model.employee.role;

import cz.cvut.omo.sem.scm.seafood.event.Event;
import cz.cvut.omo.sem.scm.seafood.model.employee.Employee;
import cz.cvut.omo.sem.scm.seafood.type.role.LaborRoleType;

public interface JobRole {

    /**
     * Executes the main duty of this role during a simulation tick.
     */
    void work(Employee context);

    /**
     * Identifies the type of role for categorization.
     */
    LaborRoleType getRoleType();

    /**
     * Handles specific events dispatched to the employee.
     * Implements the Strategy Pattern for event handling.
     *
     * Default implementation does nothing, so roles can opt-in to handle events.
     *
     * @param event The event payload (Record).
     * @param context The employee holding this role.
     */
    default void onEvent(Event event, Employee context) {
        // By default, a role ignores events.
        // Override this in specific roles (e.g., MaintenanceRole) to react to triggers.
    }
}