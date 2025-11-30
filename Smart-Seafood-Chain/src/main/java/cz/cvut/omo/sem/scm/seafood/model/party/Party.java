package cz.cvut.omo.sem.scm.seafood.model.party;

import cz.cvut.omo.sem.scm.seafood.model.SimulationEntity;
import cz.cvut.omo.sem.scm.seafood.model.item.Item;
import cz.cvut.omo.sem.scm.seafood.model.item.Material;
import cz.cvut.omo.sem.scm.seafood.model.party.role.BusinessRole;
import cz.cvut.omo.sem.scm.seafood.resource.Money;
import cz.cvut.omo.sem.scm.seafood.type.domain.MaterialType; // <--- Не забудь этот импорт!
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents any organization (Fisher, Shop, Factory).
 */
@Getter
@Setter
public class Party extends SimulationEntity {
    private Money balance;
    private List<Item> inventory = new ArrayList<>();
    // COMPOSITION: A party has a list of behaviors
    private List<BusinessRole> roles = new ArrayList<>();

    public Party(String id, String name) {
        super(id, name);
    }

    public void addRole(BusinessRole role) {
        this.roles.add(role);
    }

    /**
     * Helper for roles/recipes to find and deduct consumable materials.
     * Uses MaterialType Enum for type safety.
     *
     * @param type         The specific material Enum (e.g. SUSHI_RICE).
     * @param amountNeeded Amount in kg or units.
     * @return true if successful (deducted), false if not enough stock.
     */
    public boolean consumeMaterial(MaterialType type, double amountNeeded) {
        // Iterate through inventory to find the matching Material
        for (Item item : inventory) {
            // Check if it is a Material and matches the Enum type
            if (item instanceof Material mat && mat.getMaterialType() == type) {

                if (mat.getWeightKg() >= amountNeeded) {
                    // Deduct amount
                    mat.setWeightKg(mat.getWeightKg() - amountNeeded);

                    // Cleanup: remove from inventory if empty (with small float tolerance)
                    if (mat.getWeightKg() <= 0.001) {
                        inventory.remove(mat);
                    }
                    return true;
                }
            }
        }
        return false; // Not found or insufficient quantity
    }

    @Override
    public void handleTick() {
        // delegate logic to roles
        for (BusinessRole role : roles) {
            role.performLogic(this);
        }
    }
}