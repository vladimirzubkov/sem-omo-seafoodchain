package cz.cvut.omo.sem.scm.seafood.model.employee.role;

import cz.cvut.omo.sem.scm.seafood.model.device.vehicle.Vehicle;
import cz.cvut.omo.sem.scm.seafood.model.employee.Employee;
import cz.cvut.omo.sem.scm.seafood.type.role.LaborRoleType;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the role of an employee involved in logistics (Driver or Courier).
 * Renamed to 'LogisticsRole' to distinguish it from the 'TransportRole' of a Party.
 */
public class LogisticsRole implements JobRole {

    private final LaborRoleType specificType; // DRIVER or COURIER

    // The driver acts via a Vehicle. The capacity and speed belong to the Vehicle.
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
            findVehicle(context);
            return;
        }

        // 2. Perform logistics logic using the Vehicle's stats
        // The role manages the route, but the vehicle defines limits.
        // double currentSpeed = assignedVehicle.getMaxSpeedKmH();
        // assignedVehicle.moveTowardsDestination();

        // TODO: Handle traffic delays or rest breaks
    }

    /**
     * Logic to find an available vehicle in the Party's garage.
     */
    private void findVehicle(Employee context) {
        // TODO: Look up free vehicle in context.getParty().getDevices()
        // If specificType == DRIVER -> look for Truck/Van
        // If specificType == COURIER -> look for Bike
    }

    @Override
    public LaborRoleType getRoleType() {
        return specificType;
    }
}