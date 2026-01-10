package cz.cvut.omo.sem.scm.seafood.model.party.role;

import cz.cvut.omo.sem.scm.seafood.model.geo.SeaRegion;
import cz.cvut.omo.sem.scm.seafood.model.item.Seafood;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.type.domain.SeafoodType;
import cz.cvut.omo.sem.scm.seafood.type.operation.StorageTemperature;
import cz.cvut.omo.sem.scm.seafood.type.role.BusinessRoleType;
import cz.cvut.omo.sem.scm.seafood.simulation.Time;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

/**
 * Role responsible for catching/harvesting seafood from a specific region.
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
            return; // No catch this tick
        }

        // 2. Select a random fish type available in this region
        List<SeafoodType> availableFish = region.getSeaFoodList();
        if (availableFish.isEmpty()) return;

        SeafoodType type = availableFish.get(random.nextInt(availableFish.size()));

        // 3. Create the catch (Random weight: 5kg to 100kg for wholesale)
        double weight = 5.0 + (random.nextDouble() * 95.0);
        String batchId = "BATCH-" + System.currentTimeMillis() + "-" + random.nextInt(1000);

        Seafood catchItem = new Seafood(batchId, type, weight, true);
        catchItem.markCaught(region, Time.getCurrentTime());

        // The fish is fresh out of water (approx 5 degrees Celsius)
        catchItem.setCurrentTemperature(5.0);
        catchItem.setRequiredStorage(StorageTemperature.CHILLED);

        // 4. Add to Party's inventory
        context.getInventory().add(catchItem);

        // 5. Log the event (Console output for debugging)
        System.out.println("[PRODUCER] %s caught %.2f kg of %s in %s".formatted(
                context.getName(), weight, type, region.getName()));

        // TODO: Fire ITEM_CAUGHT event to EventBus if Party has reference to it
    }

    @Override
    public BusinessRoleType getRoleType() {
        return BusinessRoleType.PRODUCER;
    }
}