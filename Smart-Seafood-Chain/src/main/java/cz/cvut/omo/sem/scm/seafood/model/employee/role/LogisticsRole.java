package cz.cvut.omo.sem.scm.seafood.model.employee.role;

import cz.cvut.omo.sem.scm.seafood.model.device.vehicle.Vehicle;
import cz.cvut.omo.sem.scm.seafood.model.employee.Employee;
import cz.cvut.omo.sem.scm.seafood.type.operation.EventType;
import cz.cvut.omo.sem.scm.seafood.type.role.LaborRoleType;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the role of an employee involved in logistics (Driver or Courier).
 */
public class LogisticsRole implements JobRole {

    private final LaborRoleType specificType; // DRIVER or COURIER

    @Getter
    @Setter
    private Vehicle assignedVehicle;

    public LogisticsRole(LaborRoleType type) {
        this.specificType = type;
    }

    @Override
    public void work(Employee context) {
        // 1. Ensure a vehicle is assigned
        if (assignedVehicle == null) {
            // In a real scenario, we would search for a vehicle here.
            // System.out.println("[LOGISTICS] %s is waiting for a vehicle assignment.".formatted(context.getName()));
            return;
        }

        // 2. Drive the vehicle
        if (assignedVehicle.isOperational()) {
            // Driving causes the vehicle to execute its tick logic (consume fuel)
            // Note: Simulator calls handleTick on the vehicle separately if it's in the entities list.
            // But if the driver "controls" it, we might simulate route progress here.

            // Check fuel/energy implicitly via vehicle status
            // System.out.println("[LOGISTICS] %s is driving %s.".formatted(context.getName(), assignedVehicle.getName()));
        } else {
            context.reportAction(EventType.DEVICE_BREAKDOWN,
                    "Vehicle " + assignedVehicle.getName() + " is broken!", assignedVehicle.getId());
        }
    }

    @Override
    public LaborRoleType getRoleType() {
        return specificType;
    }
}