package cz.cvut.omo.sem.scm.seafood.model.employee.role;

import cz.cvut.omo.sem.scm.seafood.event.Event;
import cz.cvut.omo.sem.scm.seafood.model.employee.Employee;
import cz.cvut.omo.sem.scm.seafood.type.operation.EventType;
import cz.cvut.omo.sem.scm.seafood.type.role.LaborRoleType;

/**
 * Role representing physical labor performed by an employee.
 */
public class ManualLaborRole implements JobRole {

    private boolean isResourceAvailable = true;

    @Override
    public void work(Employee context) {
        // Check if there are resources to work with
        if (!isResourceAvailable) {
            return;
        }

        // WORK LOGIC:
        // We REMOVED context.reportAction() from here.
        // The worker is still "working" (taking time/energy),
        // but the specific production details are now handled by ProcessorRole
        // to provide much cleaner and more detailed reports.

        if (Math.random() < 0.10) {
            // Physical effort happens here, but we don't spam the global log.
        }
    }

    @Override
    public LaborRoleType getRoleType() {
        return LaborRoleType.MANUAL_LABOR;
    }

    @Override
    public void onEvent(Event event, Employee context) {
        // React to global events like warehouse depletion
        if (event.type() == EventType.RESOURCE_DEPLETED) {
            this.isResourceAvailable = false;
        } else if (event.type() == EventType.RESOURCE_REFILLED) {
            this.isResourceAvailable = true;
        }
    }
}