package cz.cvut.omo.sem.scm.seafood.model.employee.role;

import cz.cvut.omo.sem.scm.seafood.event.Event;
import cz.cvut.omo.sem.scm.seafood.model.employee.Employee;
import cz.cvut.omo.sem.scm.seafood.type.operation.EventType;
import cz.cvut.omo.sem.scm.seafood.type.role.LaborRoleType;

public class MaintenanceRole implements JobRole {

    @Override
    public void work(Employee context) {
        // 1. Check inbox for breakdown alerts
        Event alarm = context.getInbox().poll();

        if (alarm != null && alarm.type() == EventType.DEVICE_BREAKDOWN) {
            String deviceId = alarm.sourceId();
            System.out.println("[MAINTENANCE] %s received ticket for Device %s: %s".formatted(
                    context.getName(), deviceId, alarm.description()));

            // 2. Simulate repair work
            // In a complex sim, we would locate the Device object and call .visit(this) or setState.
            // Here we broadcast that repair has started/finished.

            context.reportAction(EventType.DEVICE_REPAIR_STARTED,
                    "Technician arriving at " + deviceId, deviceId);

            // Assume instant fix for simplicity in this tick, or schedule it
            context.reportAction(EventType.DEVICE_REPAIRED,
                    "Fixed " + deviceId, deviceId);

        } else if (alarm != null) {
            // Put back non-relevant messages or ignore
            // context.getInbox().add(alarm);
        } else {
            // No work
            // System.out.println("[MAINTENANCE] %s is idle.".formatted(context.getName()));
        }
    }

    @Override
    public LaborRoleType getRoleType() {
        return LaborRoleType.MAINTENANCE;
    }
}