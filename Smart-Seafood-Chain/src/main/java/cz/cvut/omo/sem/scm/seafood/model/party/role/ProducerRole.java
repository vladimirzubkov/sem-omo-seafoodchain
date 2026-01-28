package cz.cvut.omo.sem.scm.seafood.model.party.role;

import cz.cvut.omo.sem.scm.seafood.model.geo.SeaRegion;
import cz.cvut.omo.sem.scm.seafood.model.item.Seafood;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.type.domain.SeafoodType;
import cz.cvut.omo.sem.scm.seafood.type.operation.StorageTemperature;
import cz.cvut.omo.sem.scm.seafood.type.role.BusinessRoleType;
import cz.cvut.omo.sem.scm.seafood.simulation.Time;

import java.util.List;
import java.util.Random;

/**
 * Represents a single fishing unit (e.g., a vessel or fleet) operating in ONE
 * specific region.
 * For multi-region companies, attach multiple instances of this role.
 */
public class ProducerRole implements BusinessRole {

    private final SeaRegion region;
    private final double catchProbability;
    private final Random random = new Random();

    public ProducerRole(SeaRegion region, double catchProbability) {
        this.region = region;
        this.catchProbability = catchProbability;
    }

    @Override
    public void performLogic(Party context) {
        // 1. Simulation of luck (fishing probability)
        if (random.nextDouble() > catchProbability) {
            return; // No catch this hour
        }

        // 2. Select random fish from THIS region
        List<SeafoodType> availableFish = region.getSeaFoodList();
        if (availableFish.isEmpty())
            return;

        SeafoodType type = availableFish.get(random.nextInt(availableFish.size()));

        // 3. Generate Catch (Simulating a haul)
        // Weight depends on season
        double weight = (10.0 + random.nextDouble() * 90.0) * region.getSeasonMultiplier();

        Seafood catchItem = new Seafood("CATCH-" + System.nanoTime(), type, weight, true);
        catchItem.markCaught(region, Time.getCurrentTime());

        // Fresh fish settings
        catchItem.setCurrentTemperature(4.0);
        catchItem.setRequiredStorage(StorageTemperature.CHILLED);

        // 4. Add to Inventory
        context.getInventory().add(catchItem);

        String description = "[PRODUCER] %s caught %.1f kg of %s in %s".formatted(
                context.getName(), weight, type, region.getName());

        System.out.println(description);

        cz.cvut.omo.sem.scm.seafood.event.EventBus.getInstance().publish(new cz.cvut.omo.sem.scm.seafood.event.Event(
                null,
                cz.cvut.omo.sem.scm.seafood.type.operation.EventType.ITEM_CAUGHT,
                context.getName(),
                "Inventory",
                description,
                catchItem));
    }

    @Override
    public BusinessRoleType getRoleType() {
        return BusinessRoleType.PRODUCER;
    }
}