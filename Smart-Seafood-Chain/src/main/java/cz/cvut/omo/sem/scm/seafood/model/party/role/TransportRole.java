package cz.cvut.omo.sem.scm.seafood.model.party.role;

import cz.cvut.omo.sem.scm.seafood.model.item.Item;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.type.role.BusinessRoleType;

public class TransportRole implements BusinessRole {

    private final double speedKmH;

    public TransportRole(double speedKmH) {
        this.speedKmH = speedKmH;
    }

    @Override
    public void performLogic(Party context) {
        // In this simulation, if a party has TransportRole (e.g., Distributor),
        // items in their inventory are considered "In Transit".

        if (!context.getInventory().isEmpty()) {
            // Simulate logistics activity
            for (Item item : context.getInventory()) {
                // Ensure items are kept cool during transport (Mocking a refrigerated truck)
                // If we had Vehicles linked, we would use Vehicle temp here.
                // Defaulting to 2.0 degrees (Chilled) for transport.
                item.recordTemperature(2.0);
            }

            System.out.println("[TRANSPORT] %s is moving %d items at %.0f km/h".formatted(
                    context.getName(), context.getInventory().size(), speedKmH));
        }
    }

    @Override
    public BusinessRoleType getRoleType() {
        return BusinessRoleType.TRANSPORT;
    }
}