package cz.cvut.omo.sem.scm.seafood.model.party.role;

import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.type.operation.StorageTemperature;
import cz.cvut.omo.sem.scm.seafood.type.role.BusinessRoleType;

public class StorageRole implements BusinessRole {

    private final double capacityKg;
    private final StorageTemperature targetTemperature;

    public StorageRole(double capacityKg, StorageTemperature targetTemperature) {
        this.capacityKg = capacityKg;
        this.targetTemperature = targetTemperature;
    }

    @Override
    public void performLogic(Party context) {
        // TODO: Implement storage logic
        // 1. Update item temperatures
        // 2. Consume electricity
        // 3. Check for spoilage
    }

    @Override
    public BusinessRoleType getRoleType() {
        return BusinessRoleType.STORAGE;
    }
}