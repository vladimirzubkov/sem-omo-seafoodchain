package cz.cvut.omo.sem.scm.seafood.model.party.role;

import cz.cvut.omo.sem.scm.seafood.model.item.Seafood;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.resource.Money;
import cz.cvut.omo.sem.scm.seafood.simulation.Time;
import cz.cvut.omo.sem.scm.seafood.type.domain.SeafoodType;
import cz.cvut.omo.sem.scm.seafood.type.operation.StorageTemperature;
import cz.cvut.omo.sem.scm.seafood.type.role.BusinessRoleType;

import java.util.Random;

/**
 * Simulates an Importer who purchases goods from the global market
 * periodically.
 * Unlike a Producer, this consumes Money but guarantees a large batch arrival.
 */
public class ImporterRole implements BusinessRole {

    private final int periodHours; // e.g., 168 hours = 1 week
    private int lastImportHour = -999; // To trigger immediately on start
    private final Random random = new Random();

    public ImporterRole(int periodHours) {
        this.periodHours = periodHours;
    }

    @Override
    public void performLogic(Party context) {
        int currentHour = Time.getCurrentHour();

        // 1. Check Schedule
        if (currentHour - lastImportHour < periodHours) {
            return; // Not time yet
        }

        // 2. Perform Import (Buy "Container")
        // Logic: Import 500-1000kg of Salmon or Tuna (typical import goods)
        double batchWeight = 500.0 + (random.nextDouble() * 500.0);

        // Cost calculation (Global market price, e.g., 50 CZK/kg)
        Money estimatedCost = Money.czk(batchWeight * 50.0);

        // 3. Check funds (Importers perform business transactions)
        if (context.getBalance().getAmount().compareTo(estimatedCost.getAmount()) < 0) {
            System.out.println("[IMPORTER] %s cannot afford import batch (Needs %s)".formatted(
                    context.getName(), estimatedCost));
            return; // Skip this shipment due to lack of funds
        }

        // 4. Transaction
        context.setBalance(context.getBalance().subtract(estimatedCost));
        lastImportHour = currentHour;

        // 5. Create Goods (Usually Frozen for transport)
        Seafood container = new Seafood("IMPORT-" + System.nanoTime(), SeafoodType.SALMON, batchWeight, true);
        container.setFromImport(true); // Helper flag if you have one, or just logic
        container.setCurrentTemperature(-18.0);
        container.setRequiredStorage(StorageTemperature.FROZEN);

        context.getInventory().add(container);

        String description = "[IMPORTER] %s received shipment: %.1f kg of Salmon. Cost: %s".formatted(
                context.getName(), batchWeight, estimatedCost);

        System.out.println(description);

        cz.cvut.omo.sem.scm.seafood.event.EventBus.getInstance().publish(new cz.cvut.omo.sem.scm.seafood.event.Event(
                null,
                cz.cvut.omo.sem.scm.seafood.type.operation.EventType.DELIVERY_ARRIVED,
                context.getName(),
                "Inventory",
                description,
                container));
    }

    @Override
    public BusinessRoleType getRoleType() {
        return BusinessRoleType.IMPORTER;
    }
}