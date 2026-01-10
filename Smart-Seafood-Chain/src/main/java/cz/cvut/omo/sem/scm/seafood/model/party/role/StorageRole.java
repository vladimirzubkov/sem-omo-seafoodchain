package cz.cvut.omo.sem.scm.seafood.model.party.role;

import cz.cvut.omo.sem.scm.seafood.model.item.Item;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.type.operation.StorageTemperature;
import cz.cvut.omo.sem.scm.seafood.type.role.BusinessRoleType;

import java.util.ArrayList;
import java.util.List;

public class StorageRole implements BusinessRole {

    private final double capacityKg; // Not strictly used yet, but good for future logic
    private final StorageTemperature targetTemperature;

    public StorageRole(double capacityKg, StorageTemperature targetTemperature) {
        this.capacityKg = capacityKg;
        this.targetTemperature = targetTemperature;
    }

    @Override
    public void performLogic(Party context) {
        // Iterate over all items in inventory (Snapshot not strictly needed if we don't remove, but safer)
        List<Item> inventory = new ArrayList<>(context.getInventory());

        for (Item item : inventory) {
            // 1. Update item temperature based on storage settings
            item.recordTemperature(targetTemperature.getOptimal());

            // 2. Check for spoilage (FRQ7)
            if (item.getQualityLevel() < 20.0) {
                System.out.println("[STORAGE] ALERT: Item %s is spoiled! Quality: %.1f%%".formatted(
                        item.getName(), item.getQualityLevel()));
                // Logic to discard spoiled item could be added here
            }
        }

        // TODO: Here we could also consume electricity based on capacityKg
    }

    @Override
    public BusinessRoleType getRoleType() {
        return BusinessRoleType.STORAGE;
    }
}