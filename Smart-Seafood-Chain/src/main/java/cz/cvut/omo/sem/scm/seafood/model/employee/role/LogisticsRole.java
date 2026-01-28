package cz.cvut.omo.sem.scm.seafood.model.employee.role;

import cz.cvut.omo.sem.scm.seafood.event.Event;
import cz.cvut.omo.sem.scm.seafood.model.device.vehicle.Vehicle;
import cz.cvut.omo.sem.scm.seafood.model.employee.Employee;
import cz.cvut.omo.sem.scm.seafood.type.device.VehicleCategory;
import cz.cvut.omo.sem.scm.seafood.type.operation.EventType;
import cz.cvut.omo.sem.scm.seafood.type.role.LaborRoleType;
import lombok.Getter;

public class LogisticsRole implements JobRole {

    private final LaborRoleType specificType; // DRIVER or COURIER

    @Getter
    private Vehicle assignedVehicle;

    public LogisticsRole(LaborRoleType type) {
        this.specificType = type;
    }

    /**
     * New logic: Check Vehicle Category
     */
    public void setAssignedVehicle(Vehicle vehicle) {
        VehicleCategory category = vehicle.getCategory();

        // 1. Logic for COURIERS
        if (specificType == LaborRoleType.COURIER && category == VehicleCategory.HEAVY_DUTY) {
            System.out.println("[ERROR] Courier cannot drive Heavy Truck!");
            return;
        }

        // 2. Logic for DRIVERS
        if (specificType == LaborRoleType.DRIVER && category == VehicleCategory.MANUAL_POWER) {
            System.out.println("[WARNING] Driver assigned to Bicycle?");
        }
        this.assignedVehicle = vehicle;
    }

    @Override
    public void work(Employee context) {
        if (assignedVehicle != null && !assignedVehicle.isOperational()) {
            context.reportAction(EventType.DEVICE_BREAKDOWN,
                    "Vehicle failure!", assignedVehicle.getId());
        }
    }

    @Override
    public void onEvent(Event event, Employee context) {
        if (event.type() == EventType.ORDER_PLACED) {
            System.out.println("[LOGISTICS] %s received new delivery order: %s".formatted(context.getName(), event.sourceId()));
        }
    }

    @Override
    public LaborRoleType getRoleType() {
        return specificType;
    }
}