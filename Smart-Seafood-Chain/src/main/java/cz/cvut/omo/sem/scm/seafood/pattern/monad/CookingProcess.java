package cz.cvut.omo.sem.scm.seafood.pattern.monad;

import cz.cvut.omo.sem.scm.seafood.model.item.Item;
import cz.cvut.omo.sem.scm.seafood.model.item.PackageContainer;
import cz.cvut.omo.sem.scm.seafood.model.party.Party;
import cz.cvut.omo.sem.scm.seafood.type.device.RobotCapability;
import cz.cvut.omo.sem.scm.seafood.type.domain.MaterialType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * CookingProcess Monad.
 * Wraps an Item and passes it through a sequence of processing steps.
 * Implements "Railway Oriented Programming": separate tracks for Success and Failure.
 *
 * @param <T> The type of the Item currently being processed.
 */
public class CookingProcess<T extends Item> {

    private final T item;
    private final boolean isRuined;
    private final List<String> historyLog;
    private final List<RobotCapability> requiredCapabilities;

    private CookingProcess(T item, boolean isRuined, List<String> log, List<RobotCapability> caps) {
        this.item = item;
        this.isRuined = isRuined;
        this.historyLog = new ArrayList<>(log);
        this.requiredCapabilities = new ArrayList<>(caps);
    }

    // --- 1. UNIT (Return) ---
    public static <T extends Item> CookingProcess<T> begin(T rawIngredient) {
        return new CookingProcess<>(rawIngredient, false, new ArrayList<>(), new ArrayList<>());
    }

    // --- 2. BIND (FlatMap) ---
    public <R extends Item> CookingProcess<R> transform(Function<T, R> transformer, String stepName) {
        if (isRuined) return new CookingProcess<>(null, true, historyLog, requiredCapabilities);

        try {
            R newItem = transformer.apply(item);
            historyLog.add("TRANSFORM: " + stepName);
            return new CookingProcess<>(newItem, false, historyLog, requiredCapabilities);
        } catch (Exception e) {
            return fail("Exception during " + stepName + ": " + e.getMessage());
        }
    }

    // --- 3. MAP ---
    public CookingProcess<T> process(RobotCapability capability, Function<T, T> action) {
        if (isRuined) return this;
        requiredCapabilities.add(capability);
        try {
            T processedItem = action.apply(item);
            historyLog.add("ACTION: " + capability.name());
            return new CookingProcess<>(processedItem, false, historyLog, requiredCapabilities);
        } catch (Exception e) {
            return fail("Processing failed at " + capability);
        }
    }

    // --- 4. CHECKS ---
    public CookingProcess<T> ensure(Predicate<T> validator, String errorMessage) {
        if (isRuined) return this;
        if (!validator.test(item)) {
            return fail("QC FAILED: " + errorMessage);
        }
        historyLog.add("QC PASSED: " + errorMessage);
        return this;
    }

    // --- 5. INVENTORY OPERATIONS ---
    /**
     * Attempts to fetch a required ingredient from the inventory using Enum type.
     */
    public CookingProcess<T> requireIngredient(Party context, MaterialType type, double amount) {
        if (isRuined) return this;

        // Delegates to Party to find and consume the material by Enum
        boolean consumed = context.consumeMaterial(type, amount);

        if (!consumed) {
            return fail("MISSING MATERIAL: %s (%.3f kg)".formatted(type.getPrettyName(), amount));
        }

        historyLog.add("ADDED: %s (%.3f kg)".formatted(type.getPrettyName(), amount));
        return this;
    }

    // --- 6. PACKAGING SUPPORT ---
    public CookingProcess<PackageContainer> packInto(String boxType, double tareWeight) {
        return transform(currentItem -> {
            PackageContainer box = new PackageContainer(currentItem.getItemId() + "-BOX", boxType, tareWeight);
            box.addItem(currentItem);
            box.setCurrentTemperature(currentItem.getCurrentTemperature());
            return box;
        }, "Packaging into " + boxType);
    }

    private <R extends Item> CookingProcess<R> fail(String reason) {
        List<String> newLog = new ArrayList<>(historyLog);
        newLog.add("FAILURE: " + reason);
        return new CookingProcess<>(null, true, newLog, requiredCapabilities);
    }

    public CookingResult<T> complete() {
        return new CookingResult<>(item, !isRuined, historyLog, requiredCapabilities);
    }

    public record CookingResult<T>(T resultItem, boolean success, List<String> logs, List<RobotCapability> requirements) {}
}