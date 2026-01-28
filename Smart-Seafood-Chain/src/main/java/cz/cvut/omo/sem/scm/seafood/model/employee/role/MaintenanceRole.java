package cz.cvut.omo.sem.scm.seafood.model.employee.role;

import cz.cvut.omo.sem.scm.seafood.event.Event;
import cz.cvut.omo.sem.scm.seafood.event.EventBus;
import cz.cvut.omo.sem.scm.seafood.event.EventListener;
import cz.cvut.omo.sem.scm.seafood.pattern.builder.EventBuilder;
import cz.cvut.omo.sem.scm.seafood.model.employee.Employee;
import cz.cvut.omo.sem.scm.seafood.type.operation.EventType;
import cz.cvut.omo.sem.scm.seafood.type.role.LaborRoleType;

import java.util.LinkedList;
import java.util.Queue;

public class MaintenanceRole implements JobRole, EventListener {

    // Local task queue for the technician
    private final Queue<String> repairQueue = new LinkedList<>();

    public MaintenanceRole() {
        // Subscribe to breakdowns globally
        EventBus.getInstance().subscribe(EventType.DEVICE_BREAKDOWN, this);
    }

    @Override
    public void work(Employee context) {
        // 1. Check if there are tasks
        if (repairQueue.isEmpty()) {
            return;
        }

        // 2. Peek at the task (don't remove yet)
        String brokenDeviceId = repairQueue.peek();

        // 3. Check if this device belongs to MY employer (Local check)
        // We cannot fix devices that are in other cities!
        boolean isLocalDevice = false;

        if (context.getEmployer() != null && context.getEmployer().getDevices() != null) {
            isLocalDevice = context.getEmployer().getDevices().stream()
                    .anyMatch(device -> device.getId().equals(brokenDeviceId));
        }

        // 4. Remove from queue regardless (processed)
        repairQueue.poll();

        if (!isLocalDevice) {
            // Log for debug (optional) or just ignore
            return;
        }

        // 5. Fix & Log
        System.out.println("[MAINTENANCE] %s is repairing device %s...".formatted(context.getName(), brokenDeviceId));

        context.reportAction(EventType.DEVICE_REPAIR_STARTED,
                "Technician arriving at " + brokenDeviceId, brokenDeviceId);

        // --- Add Technician Name and ID to the report description ---
        String reportDescription = "[Staff] %s (%s) fixed device %s".formatted(
                context.getName(),
                context.getId(),
                brokenDeviceId
        );

        // 6. Publish Global Event
        EventBus.getInstance().publish(new EventBuilder()
                .type(EventType.MAINTENANCE_PERFORMED)
                .sourceId(context.getId())
                .description(reportDescription) // Use a new description
                .build());

        // 7. Local Log
        context.reportAction(EventType.DEVICE_REPAIRED, reportDescription, brokenDeviceId);
    }

    @Override
    public LaborRoleType getRoleType() {
        return LaborRoleType.MAINTENANCE;
    }

    @Override
    public void onEvent(Event event, Employee context) {
        // Delegate to handleEvent
        handleEvent(event);
    }

    @Override
    public void handleEvent(Event event) {
        if (event.type() == EventType.DEVICE_BREAKDOWN) {
            System.out.println("[URGENT] Technician accepted repair ticket for device: %s"
                    .formatted(event.sourceId()));

            // Add the broken device to the internal to-do list
            repairQueue.add(event.sourceId());
        }
    }
}