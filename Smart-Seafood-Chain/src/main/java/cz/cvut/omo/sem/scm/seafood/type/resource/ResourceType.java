package cz.cvut.omo.sem.scm.seafood.type.resource;

import lombok.Getter;

@Getter
public enum ResourceType {
    // Energy
    ELECTRICITY("kWh"),

    // Water
    WATER("Liters"),

    // Fuels (Replaces FuelType)
    DIESEL("Liters"),
    PETROL("Liters"),

    // Human Energy (for couriers)
    CALORIES("kcal");

    private final String unit;

    ResourceType(String unit) {
        this.unit = unit;
    }
}