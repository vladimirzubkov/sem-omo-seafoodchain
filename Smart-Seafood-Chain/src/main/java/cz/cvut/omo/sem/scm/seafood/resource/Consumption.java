package cz.cvut.omo.sem.scm.seafood.resource;

import cz.cvut.omo.sem.scm.seafood.type.resource.ResourceType;
import lombok.Value;

/**
 * Represents a quantity of a specific resource.
 * Replaces separate classes like Water, Electricity.
 */
@Value
public class Consumption {
    ResourceType type;
    double amount;

    public static Consumption of(ResourceType type, double amount) {
        return new Consumption(type, amount);
    }

    @Override
    public String toString() {
        return String.format("%.2f %s of %s", amount, type.getUnit(), type.name());
    }
}