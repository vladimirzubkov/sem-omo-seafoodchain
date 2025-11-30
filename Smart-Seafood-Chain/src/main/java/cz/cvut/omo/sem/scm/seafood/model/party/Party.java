package cz.cvut.omo.sem.scm.seafood.model.party;

import cz.cvut.omo.sem.scm.seafood.model.SimulationEntity;
import cz.cvut.omo.sem.scm.seafood.model.item.Item;
import cz.cvut.omo.sem.scm.seafood.model.party.role.BusinessRole;
import cz.cvut.omo.sem.scm.seafood.resource.Money;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents any organization (Fisher, Shop, Factory).
 * Fixes: "Inheritance Trap". One Party can now have multiple roles (e.g., Fisher AND Seller).
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

    @Override
    public void handleTick() {
        // delegate logic to roles
        for (BusinessRole role : roles) {
            role.performLogic(this);
        }
    }
}