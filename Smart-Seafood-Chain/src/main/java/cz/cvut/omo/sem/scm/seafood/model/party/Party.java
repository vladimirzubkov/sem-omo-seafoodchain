package cz.cvut.omo.sem.scm.seafood.model.party;

import cz.cvut.omo.sem.scm.seafood.blockchain.Blockchain;
import cz.cvut.omo.sem.scm.seafood.event.EventBus;
import cz.cvut.omo.sem.scm.seafood.model.SimulationEntity;
import cz.cvut.omo.sem.scm.seafood.model.device.Device;
import cz.cvut.omo.sem.scm.seafood.model.employee.Employee;
import cz.cvut.omo.sem.scm.seafood.model.item.Item;
import cz.cvut.omo.sem.scm.seafood.model.item.Material;
import cz.cvut.omo.sem.scm.seafood.model.party.role.BusinessRole;
import cz.cvut.omo.sem.scm.seafood.pattern.chain.OrderHandler;
import cz.cvut.omo.sem.scm.seafood.pattern.prototype.Prototype;
import cz.cvut.omo.sem.scm.seafood.pattern.visitor.EntityVisitor;
import cz.cvut.omo.sem.scm.seafood.pattern.visitor.Visitable;
import cz.cvut.omo.sem.scm.seafood.resource.Money;
import cz.cvut.omo.sem.scm.seafood.type.domain.MaterialType;
import cz.cvut.omo.sem.scm.seafood.type.resource.Currency;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents any organization (Fisher, Shop, Factory).
 * Implements PROTOTYPE (Cloneable) for Memento.
 */
@Getter
@Setter
public class Party extends SimulationEntity implements Visitable, Prototype<Party> {
    private Money balance;
    private Currency currency = Currency.USD; // Default value
    private String type; // e.g. "Fisher", "Processor" - populated from config
    private List<Item> inventory = new ArrayList<>();
    private List<BusinessRole> roles = new ArrayList<>();

    // lists for Inspectors and Visitors
    private List<Device> devices = new ArrayList<>();
    private List<Employee> employees = new ArrayList<>();

    private Blockchain blockchain;
    private EventBus eventBus;

    // CHAIN OF RESPONSIBILITY: The next link in the supply chain to request goods from
    private OrderHandler orderHandler;

    public Party(String id, String name) {
        super(id, name);
    }

    // safe getter
    public String getType() {
        return this.type != null ? this.type : "";
    }

    // --- CHAIN OF RESPONSIBILITY METHOD ---
    public void requestGoods(String itemType, double amount, Money maxPrice) {
        if (orderHandler != null) {
            System.out.println("[ORDER] %s places order for %s".formatted(this.getName(), itemType));
            orderHandler.handleOrder(this, itemType, amount, maxPrice);
        } else {
            System.out.println("[ORDER] %s has no suppliers linked to place orders!".formatted(this.getName()));
        }
    }

    @Override
    public void accept(EntityVisitor visitor) {
        visitor.visit(this);
    }

    public void addRole(BusinessRole role) {
        this.roles.add(role);
    }

    public boolean consumeMaterial(MaterialType type, double amountNeeded) {
        for (Item item : inventory) {
            if (item instanceof Material mat && mat.getMaterialType() == type) {
                if (mat.getWeightKg() >= amountNeeded) {
                    mat.setWeightKg(mat.getWeightKg() - amountNeeded);
                    if (mat.getWeightKg() <= 0.001) {
                        inventory.remove(mat);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void handleTick() {
        for (BusinessRole role : roles) {
            role.performLogic(this);
        }
    }

    // --- PROTOTYPE PATTERN (Deep Copy) ---
    @Override
    public Party clone() {
        try {
            // Shallow copy of primitives
            Party cloned = (Party) super.clone();

            // Deep copy of Inventory is CRITICAL for Memento
            // Otherwise, changes in the future will affect the saved snapshot
            cloned.inventory = new ArrayList<>();
            for (Item item : this.inventory) {
                cloned.inventory.add(item.clone());
            }

            // Roles are mostly stateless strategies, but we copy the list structure
            cloned.roles = new ArrayList<>(this.roles);

            // References to singletons (Blockchain, EventBus) remain shared (correct for this context)
            return cloned;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}