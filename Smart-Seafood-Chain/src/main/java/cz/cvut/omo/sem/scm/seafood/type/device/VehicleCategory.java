package cz.cvut.omo.sem.scm.seafood.type.device;

/**
 * Defines the class/license requirement for a vehicle.
 * Eliminates the need for instanceof checks in business logic.
 */
public enum VehicleCategory {
    HEAVY_DUTY,      // Trucks, big lorries (Requires Driver)
    LIGHT_COMMERCIAL, // Vans, cars (Driver or Courier)
    MANUAL_POWER     // Bicycles, scooters (Courier, typically)
}